package thread;

import game.Level;
import game.entity.mob.player.Player;
import graphics.Texture;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import render.BGRender;
import render.Cache;
import render.EntityRender;
import render.PlayerRender;
import render.Screen;

public class RenderMaster {

    public static final ArrayList<BufferedImage> rendered = new ArrayList<>();

    public static int maxFrames;
    private static int lastX, lastY;
    private static int scrTilesWidth, scrTilesHeight;

    protected RenderMaster() {
        maxFrames = 5;
        scrTilesWidth = Screen.WIDTH / Level.SIZE / Level.ZOOM + 2;
        scrTilesHeight = Screen.HEIGHT / Level.SIZE / Level.ZOOM + 2;
    }

    protected boolean hasRendered() {
        return !rendered.isEmpty();
    }

    protected BufferedImage getFrame() {
        //while (rendered.isEmpty()){}
        BufferedImage frame = rendered.remove(0);
        return frame;
    }

    public static final int size() {
        return rendered.size();
    }

    public void render() {
        BufferedImage b = Cache.newFrame();
        Player p = TimerMaster.getClientPlayer();
        Texture t = p.getTexture();
        int xOff = (int) (Level.ZOOM * (p.getXDouble() + t.getWidth() / 2.) - Screen.WIDTH / 2.);
        int yOff = (int) (Level.ZOOM * (p.getYDouble() + t.getHeight() / 2.) - Screen.HEIGHT / 2.);
        BGRender.render(b, xOff, yOff);
        PlayerRender.render(b, xOff, yOff);
        EntityRender.render(b, xOff, yOff);
        rendered.add(b);

        lastX = xOff / Level.SIZE / Level.ZOOM - 1;
        lastY = yOff / Level.SIZE / Level.ZOOM - 1;
    }

    public static boolean isOnTileScreen(int x, int y) {
        return x > lastX && y > lastY && x < lastX + scrTilesWidth && y < lastY + scrTilesHeight;
    }
}
