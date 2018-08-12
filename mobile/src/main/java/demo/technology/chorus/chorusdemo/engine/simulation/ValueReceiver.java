package demo.technology.chorus.chorusdemo.engine.simulation;

import java.util.concurrent.ThreadLocalRandom;

import demo.technology.chorus.chorusdemo.model.RatingModel;

public class ValueReceiver {

    public static RatingModel updateRatingModel(RatingModel ratingModel) {
        ratingModel.setAccelerationRating(generateNextDouble());
        ratingModel.setBreakingRating(generateNextDouble());
        ratingModel.setPhoningRating(generateNextDouble());
        ratingModel.setSpeedingRating(generateNextDouble());
        ratingModel.setMainDriverRating((ratingModel.getAccelerationRating() + ratingModel.getPhoningRating() + ratingModel.getBreakingRating() + ratingModel.getSpeedingRating()) / 4.0d);
        return ratingModel;
    }

    private static double generateNextDouble() {
        return ThreadLocalRandom.current().nextDouble(7, 10);
    }
}
