package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.constant.State;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.EventHandler;
import com.mygdx.game.event.events.CharacterSelectedEvent;
import com.mygdx.game.event.events.SocketConnectedEvent;
import com.mygdx.game.factory.TextureConfigFactory;
import com.mygdx.game.model.AnimConfig;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;
import java.util.List;

@Singleton
public class CharacterSelectScreen implements Screen, EventHandler {

    private final EventBus eventBus;

    private final Stage stage;

    private final Skin skin;

    private final Map<CharacterConstants.CharacterType, TextureRegion> characterAnimMap;

    private Cell<Image> characterImageCell;

    private Cell<TextField> characterNameCell;

    private int currentCharacterIndex = 0;

    @Inject
    public CharacterSelectScreen(@NonNull final EventBus eventBus,
                                 @NonNull final Stage stage,
                                 @Named(AppConstants.CHARACTER_SELECT) @NonNull final Skin skin,
                                 @NonNull final TextureConfigFactory textureConfigFactory) {
        this.eventBus = eventBus;
        this.stage = stage;
        this.skin = skin;
        this.characterAnimMap = new HashMap<>();
        textureConfigFactory.getCharacterTypeTextureConfigMap().forEach((characterType, textureConfig) -> {
            TextureAtlas textureAtlas = new TextureAtlas(textureConfig.getTexturePath());
            AnimConfig animConfig = textureConfig.getAnimConfigMap().get(State.IDLE);
            characterAnimMap.put(characterType, textureAtlas.findRegions(animConfig.getName()).get(0));
        });
        eventBus.subscribe(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = getTable();
        TextButton previousButton = getPreviousButton();
        TextButton nextButton = getNextButton();
        TextButton startButton = getStartButton();
        Image characterImage = new Image(characterAnimMap.get(CharacterConstants.CharacterType.values()[currentCharacterIndex]));
        TextField characterNameTextField = new TextField("", this.skin);

        addItemsToTable(table, previousButton, nextButton, startButton, characterImage, characterNameTextField);

        stage.addActor(table);
    }

    private void addItemsToTable(final Table table,
                                 final TextButton previousButton,
                                 final TextButton nextButton,
                                 final TextButton startButton,
                                 final Image characterImage,
                                 final TextField characterNameTextField) {
        table.add(previousButton).padTop(Gdx.graphics.getHeight()/2f).padBottom(20).padLeft(80).align(Align.center);
        characterImageCell = table.add(characterImage).padTop(Gdx.graphics.getHeight()/2.9f).padBottom(20).align(Align.center);
        table.add(nextButton).padTop(Gdx.graphics.getHeight()/2f).padBottom(20).padRight(80).align(Align.center);
        table.row();
        table.add(new Label("Character name", this.skin)).colspan(3);
        table.row();
        characterNameCell = table.add(characterNameTextField).colspan(3).align(Align.center).padBottom(20);
        table.row();
        table.add(startButton).colspan(3).align(Align.center);
    }

    private TextButton getStartButton() {
        TextButton startButton = new TextButton("Start", this.skin, "small");
        startButton.setHeight(30);
        startButton.setWidth(100);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (Objects.isNull(characterNameCell.getActor().getText()) || characterNameCell.getActor().getText().isEmpty()) {
                    Gdx.app.log(AppConstants.APP_LOG_TAG, "Must enter name"); // TODO : Popup error message
                } else {
                    eventBus.publish(CharacterSelectedEvent.builder()
                            .characterSelectedPayload(CharacterSelectedEvent.CharacterSelectedPayload.builder()
                                    .character(CharacterConstants.CharacterType.values()[currentCharacterIndex])
                                    .name(characterNameCell.getActor().getText())
                                    .build())
                            .build());
                }
            }
        });
        return startButton;
    }

    private TextButton getNextButton() {
        TextButton nextButton = new TextButton(">", this.skin, "small");
        nextButton.setHeight(30);
        nextButton.setWidth(30);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                currentCharacterIndex = (currentCharacterIndex + 1) % CharacterConstants.CharacterType.values().length;
                characterImageCell.setActor(new Image(characterAnimMap.get(CharacterConstants.CharacterType.values()[currentCharacterIndex])));
            }
        });
        return nextButton;
    }

    private TextButton getPreviousButton() {
        TextButton previousButton = new TextButton("<", this.skin, "small");
        previousButton.setHeight(30);
        previousButton.setWidth(30);
        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                currentCharacterIndex = currentCharacterIndex == 0 ?
                        CharacterConstants.CharacterType.values().length - 1 :
                        currentCharacterIndex - 1;
                characterImageCell.setActor(new Image(characterAnimMap.get(CharacterConstants.CharacterType.values()[currentCharacterIndex])));
            }
        });
        return previousButton;
    }

    private Table getTable() {
        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        return table;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public List<Class<?>> getSubscribedEventClasses() {
        return Collections.singletonList(SocketConnectedEvent.class);
    }

    @Override
    public void handleEvent(Event<?> event) {
        if (SocketConnectedEvent.class == event.getClass()) {
            handleSocketConnected((SocketConnectedEvent) event);
        }
    }

    private void handleSocketConnected(final SocketConnectedEvent event) {
        Gdx.app.log(AppConstants.APP_LOG_TAG, "Received SocketConnected");
        // TODO: Add flag such that character select is rendered only once this event is received
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }
}
