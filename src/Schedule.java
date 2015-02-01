import java.util.ArrayList;
/**
 * 
 * This is the structure you must use for the schedule
 *
 */
public class Schedule
{
    
    /* the schedule represented as an arraylist of arraylist of production steps */
    private ArrayList<ArrayList<Integer>> schedule;
    
    /**
     * The constructor of this class
     *
     * @param an integer representing the production span i.e. longest path + 1
     * @throws exception if span < 0
     */
    public Schedule(int span) {
        schedule = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i< span; i++){
            schedule.add(new ArrayList<Integer>());
        }
    }
    
    /**
     * This function adds a production step at time i to the schedule
     *
     * @param an integer representing time t
     * @param an integer representing a production step in the dag
     * @throws null pointer exception if t < 0 or t >= schedule.size()
     */
    public void addToSchedule(int t, int production_step){
        schedule.get(t).add(production_step);
    }
    
    /**
     * This function returns the value of production span of the schedule
     *
     */
    public int getProductionSpan(){
        return schedule.size();
    }
    
    /**
     * This function returns an arraylist of the production steps
     * that are scheduled to execute at time t
     *
     * @param an integer representing time t
     * @throws null pointer exception if t < 0 or t >= schedule.size()
     */
    public ArrayList<Integer> getSchedule(int t){
        return schedule.get(t);
    }
    
    

}
