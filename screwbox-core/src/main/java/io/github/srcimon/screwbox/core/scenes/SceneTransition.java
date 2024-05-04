package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.Duration.oneSecond;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_IN;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_OUT;

/**
 * Configures a scene transition. Every transition contains an extro phase (leaving a {@link Scene}) and and intro phase (entering a {@link Scene}).
 *
 * @param extroAnimation animation used for extro
 * @param extroDuration  {@link Duration} of the extro
 * @param extroEase      the {@link Ease} applied on the extro animation
 * @param introAnimation animation used for intro
 * @param introDuration  {@link Duration} of the intro
 * @param introEase      the {@link Ease} applied on the intro animation
 */
public record SceneTransition(
        ExtroAnimation extroAnimation, Duration extroDuration, Ease extroEase, IntroAnimation introAnimation,
        Duration introDuration, Ease introEase
) {

    //TODO Support incomming graphics as well scene.screenshotfrom(scene.class)
    //TODO validations

    private static final ExtroAnimation NO_EXTRO = (screen, progress) -> {

    };

    private static final IntroAnimation NO_INTRO = (screen, progress, screenshot) -> {

    };

    public static SceneTransition instant() {
        return new SceneTransition(NO_EXTRO, Duration.none(), LINEAR_IN, NO_INTRO, Duration.none(), LINEAR_OUT);
    }

    //TODO javadoc for readability
    public static SceneTransition noExtro() {
        return instant();
    }
    //TODO: split duration and animation possible?

    public static SceneTransition extroAnimation(final Supplier<ExtroAnimation> extroAnimation) {
        return extroAnimation(extroAnimation.get());
    }

    public static SceneTransition extroAnimation(final ExtroAnimation extroAnimation) {
        return new SceneTransition(extroAnimation, Duration.none(), LINEAR_IN, NO_INTRO, Duration.none(), LINEAR_OUT);
    }

    public SceneTransition ease(final Ease ease) {
        return new SceneTransition(extroAnimation, extroDuration, ease, introAnimation, introDuration, ease);
    }

    public SceneTransition extroEase(final Ease extroEase) {
        return new SceneTransition(extroAnimation, extroDuration, LINEAR_IN, introAnimation, introDuration, extroEase);
    }

    public SceneTransition introEase(final Ease introEase) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition introAnimation(final Supplier<IntroAnimation> introAnimation) {
        return introAnimation(introAnimation.get());
    }

    public SceneTransition introAnimation(final IntroAnimation introAnimation) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofMillis(millis), introEase);
    }

    public SceneTransition extroDurationMillis(final long millis) {
        return new SceneTransition(extroAnimation, Duration.ofMillis(millis), extroEase, introAnimation, introDuration, introEase);
    }
}
