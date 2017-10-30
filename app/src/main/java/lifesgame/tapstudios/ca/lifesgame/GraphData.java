package lifesgame.tapstudios.ca.lifesgame;

/**
 * Created by Vidit Soni on 8/8/2017.
 */
public class GraphData {
    public Float silver;
    public Float totalCompleted;

    public GraphData () {
        this.silver = 0F;
        this.totalCompleted = 0F;
    }

    public void addSilver(Float silverAmount) {
        silver += silverAmount;
    }

    public void addCompletedAmount(Float completedAmount) {
        totalCompleted += completedAmount;
    }

    public float getCompletedAmount() {
        return totalCompleted;
    }

    public float getSilverAmount() {
        return silver;
    }
}
