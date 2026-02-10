package com.artattack.view;

public class GameSettings {
    
    public enum TextSpeed {
        SLOW(100), NORMAL(40), FAST(10), INSTANT(0);
        private final int delay;
        TextSpeed(int delay) { this.delay = delay; }
        public int getDelay() { return delay; }
        public TextSpeed next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    public enum FontSize {
        SMALL(14),
        MEDIUM(18),
        LARGE(24),
        HUGE(30);

        private final int size;
        FontSize(int size) { this.size = size; }
        public int getSize() { return size; }
        
        public FontSize next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    public enum MapZoom {
        NORMAL(12),   
        MEDIUM(16),   
        LARGE(20),    
        HUGE(24);     

        private final int size;
        MapZoom(int size) { this.size = size; }
        public int getSize() { return size; }
        
        public MapZoom next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    private static GameSettings instance;
    private TextSpeed textSpeed;
    private FontSize fontSize; 
    private MapZoom mapZoom;

    private GameSettings() {
        this.textSpeed = TextSpeed.NORMAL;
        this.fontSize = FontSize.MEDIUM; 
        this.mapZoom = MapZoom.NORMAL;
    }

    public static synchronized GameSettings getInstance() {
        if (instance == null) instance = new GameSettings();
        return instance;
    }

    public TextSpeed getTextSpeed() { return textSpeed; }
    public void cycleTextSpeed() { this.textSpeed = this.textSpeed.next(); }

    public FontSize getFontSize() { return fontSize; }
    public void cycleFontSize() { this.fontSize = this.fontSize.next(); }


    // pixel
    public int getZoomLevel() { return mapZoom.getSize(); }
    
    public MapZoom getCurrentZoom() { return mapZoom; }
    
    public void cycleZoomLevel() { this.mapZoom = this.mapZoom.next(); }
}