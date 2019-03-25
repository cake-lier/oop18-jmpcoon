package view.game;

import model.Entity;
import utils.Pair;

public class Converter {
    private final Pair<Double, Double> worldDimensions, sceneDimensions;
    
    Converter(Pair<Double, Double> worldDimensions, Pair<Double, Double> sceneDimensions){
        this.worldDimensions=worldDimensions;
        this.sceneDimensions=sceneDimensions;
    }
    
    static DrawableEntity getDrawableEntity(Entity entity) {
        //TODO
        return null;
    }
    
}
