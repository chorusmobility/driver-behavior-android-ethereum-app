package demo.technology.chorus.chorusdemo.model;

public class RatingModel {
    private double mainDriverRating;
    private double accelerationRating;
    private double speedingRating;
    private double breakingRating;
    private double phoningRating;

    public RatingModel(double mainDriverRating, double accelerationRating, double speedingRating, double breakingRating, double phoningRating) {
        this.mainDriverRating = mainDriverRating;
        this.accelerationRating = accelerationRating;
        this.speedingRating = speedingRating;
        this.breakingRating = breakingRating;
        this.phoningRating = phoningRating;
    }

    public double getMainDriverRating() {
        return mainDriverRating;
    }

    public void setMainDriverRating(double mainDriverRating) {
        this.mainDriverRating = mainDriverRating;
    }

    public double getAccelerationRating() {
        return accelerationRating;
    }

    public void setAccelerationRating(double accelerationRating) {
        this.accelerationRating = accelerationRating;
    }

    public double getSpeedingRating() {
        return speedingRating;
    }

    public void setSpeedingRating(double speedingRating) {
        this.speedingRating = speedingRating;
    }

    public double getBreakingRating() {
        return breakingRating;
    }

    public void setBreakingRating(double breakingRating) {
        this.breakingRating = breakingRating;
    }

    public double getPhoningRating() {
        return phoningRating;
    }

    public void setPhoningRating(double phoningRating) {
        this.phoningRating = phoningRating;
    }
}
