package de.suzufa.screwbox.core.graphics;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.utils.ResourceLoader;

public class Sprite implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Sprite INVISIBLE = new Sprite(List.of(Frame.invisible()));

    private final List<Frame> frames = new ArrayList<>();
    private Dimension dimension;
    private final Time started = Time.now();
    private Duration duration = Duration.none();
    private boolean flippedHorizontally = false;
    private boolean flippedVertically = false;

    private Sprite(final Image image) {
        this(asList(new Frame(image)), false);
    }

    public Sprite(final List<Frame> frames) {
        this(frames, false);
    }

    private Sprite(final List<Frame> frames, final boolean flipped) {
        this.flippedHorizontally = flipped;
        for (final var frame : frames) {
            addFrame(frame);
        }
    }

    public static Sprite fromFile(final String fileName) {
        final var image = imageFromFile(fileName);
        return fromImage(image);
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Dimension dimension) {
        return multipleFromFile(fileName, dimension, 0);
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Dimension dimension, final int padding) {
        final var sprites = new ArrayList<Sprite>();
        for (final var subimage : extractSubImages(fileName, dimension, padding)) {
            sprites.add(fromImage(subimage));
        }
        return sprites;
    }

    private static ArrayList<Image> extractSubImages(final String fileName, final Dimension dimension,
            final int padding) {
        final var image = imageFromFile(fileName);
        final var subImages = new ArrayList<Image>();
        for (int y = 0; y + dimension.height() <= image.getHeight(); y += dimension.height() + padding) {
            for (int x = 0; x + dimension.width() <= image.getWidth(); x += dimension.width() + padding) {
                final var subimage = image.getSubimage(x, y, dimension.width(), dimension.height());
                subImages.add(subimage);
            }
        }
        return subImages;
    }

    // TODO: public static Sprite animatedFromFile(String fileName, Dimension
    // dimension, Duration duration);

    /**
     * Returns an invisible {@link Sprite}.
     */
    public static Sprite invisible() {
        return INVISIBLE;
    }

    /**
     * Returns a {@link Sprite} with one {@link Frame} containing the given
     * {@link Image}.
     */
    public static Sprite fromImage(final Image image) {
        return new Sprite(image);
    }

    public boolean isFlippedHorizontally() {
        return flippedHorizontally;
    }

    public void setFlippedHorizontally(final boolean flippedHorizontally) {
        this.flippedHorizontally = flippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(final boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
    }

    /**
     * Returns the count of {@link Frame}s in the {@link Sprite}.
     */
    public int frameCount() {
        return frames.size();
    }

    /**
     * Returns the size of the {@link Sprite}. Every {@link Frame} in the sprites
     * animation has the same size.
     */
    public Dimension size() {
        return dimension;
    }

    /**
     * Returns the {@link Image} for the given {@link Time}.
     */
    public Image getImage(final Time time) {
        final var frameNr = getFrameNr(frames, duration, time);
        return getImage(frameNr);
    }

    public Sprite newInstance() {
        return new Sprite(frames, flippedHorizontally);
    }

    private Image getImage(final int frameNr) {
        return frames.get(frameNr).image(flippedHorizontally, flippedVertically);
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the frame of the sprite if there is only a single frame in the
     * sprite.
     */
    public Frame singleFrame() {
        if (frameCount() > 1) {
            throw new IllegalStateException("The sprite has more than one frame.");
        }
        return getFrame(0);
    }

    /**
     * Returns the frame with the given number, starting with 0.
     * 
     * @throws IllegalArgumentException if the number is invalid
     */
    public Frame getFrame(final int nr) {
        if (nr < 0) {
            throw new IllegalArgumentException(nr + " is an invalid frame number");
        }
        if (nr >= frames.size()) {
            throw new IllegalArgumentException(
                    format("Cannot return frame nr %d, because sprite has only %d frame(s).", nr, frames.size()));
        }
        return frames.get(nr);
    }

    /**
     * Returns a new {@link Sprite}. The old {@link Color} in alle {@link Frame}s is
     * replaced with a new one. This method is quite slow.
     * 
     * @see Frame#replaceColor(Color, Color)
     */
    public Sprite replaceColor(final Color oldColor, final Color newColor) {
        final List<Frame> recoloredFrames = new ArrayList<>();
        for (final var frame : frames) {
            recoloredFrames.add(frame.replaceColor(oldColor, newColor));
        }
        final Sprite sprite = new Sprite(recoloredFrames);
        sprite.setFlippedHorizontally(flippedHorizontally);
        sprite.setFlippedVertically(flippedVertically);
        return sprite;
    }

    /**
     * Returns a scaled version of this {@link Sprite}.
     */
    public Sprite scaled(final double scale) {
        final List<Frame> scaledFrames = new ArrayList<>();
        for (final var frame : this.frames) {
            scaledFrames.add(frame.scaled(scale));
        }
        final Sprite newSprite = new Sprite(scaledFrames);
        newSprite.setFlippedHorizontally(flippedHorizontally);
        newSprite.setFlippedVertically(flippedVertically);
        return newSprite;
    }

    private void addFrame(final Frame frame) {
        if (isNull(dimension)) {
            dimension = frame.size();
        } else if (!dimension.equals(frame.size())) {
            throw new IllegalArgumentException("Cannot add frame with different dimension to sprite");
        }
        duration = duration.plus(frame.duration());
        frames.add(frame);
    }

    private int getFrameNr(final List<Frame> frames, final Duration durationSum, final Time time) {
        if (frames.size() == 1) {
            return 0;
        }
        final long timerIndex = Duration.between(time, started).nanos() % durationSum.nanos();
        long sumAtCurrentIndex = 0;
        long sumAtNextIndex = 0;
        for (int i = 0; i < frames.size(); i++) {
            sumAtNextIndex += frames.get(i).duration().nanos();

            if (timerIndex >= sumAtCurrentIndex && timerIndex <= sumAtNextIndex) {
                return i;
            }

            sumAtCurrentIndex += frames.get(i).duration().nanos();
        }
        return frames.size() - 1;
    }

    private static BufferedImage imageFromFile(final String fileName) {
        try {
            final File resource = ResourceLoader.resourceFile(fileName);
            final BufferedImage image = ImageIO.read(resource);
            if (isNull(image)) {
                throw new IllegalArgumentException("image cannot be read: " + fileName);
            }
            return image;

        } catch (final IOException e) {
            throw new IllegalArgumentException("error while reading image: " + fileName, e);
        }
    }

}
