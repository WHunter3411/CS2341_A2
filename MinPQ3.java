import java.util.*;
//Wade Hunter and Julia Bonsack Third PQ

public class MinPQ3 {

    static class Job {
        int id;
        int processingTime;
        int arrivalTime;

        public Job(int id, int processingTime, int arrivalTime) {
            this.id = id;
            this.processingTime = processingTime;
            this.arrivalTime = arrivalTime;
        }

        @Override
        public String toString() {
            return "Job ID: " + id + ", Time: " + processingTime + ", Arrival: " + arrivalTime;
        }
    }

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();

        try {
            while (!StdIn.isEmpty()) {
                int id = StdIn.readInt();
                int processingTime = StdIn.readInt();
                int arrivalTime = StdIn.readInt();
                jobs.add(new Job(id, processingTime, arrivalTime));
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error reading input. Ensure proper formatting.");
            return;
        }

        jobs.sort(Comparator.comparingInt(j -> j.arrivalTime));

        processAndPrint(jobs);
    }

//info
    public static void processAndPrint(List<Job> jobs) {
        PriorityQueue<Job> pq = new PriorityQueue<>(Comparator.comparingInt(j -> j.processingTime));
        int currentTime = 0;
        int totalCompletionTime = 0;
        int jobIndex = 0;
        StringBuilder executionOrder = new StringBuilder("Execution order: [");


        while (jobIndex < jobs.size() || !pq.isEmpty()) {

            while (jobIndex < jobs.size() && jobs.get(jobIndex).arrivalTime <= currentTime) {
                pq.offer(jobs.get(jobIndex));
                jobIndex++;
            }

            if (pq.isEmpty()) {
                currentTime = jobs.get(jobIndex).arrivalTime;
            } else {
                Job job = pq.poll();
                currentTime += job.processingTime;
                totalCompletionTime += currentTime;
                executionOrder.append(job.id).append(", ");
            }
        }
        executionOrder.setLength(executionOrder.length() - 2);
        executionOrder.append("]");

        double averageCompletionTime = (double) totalCompletionTime / jobs.size();

        //print stuff
        StdOut.println(executionOrder);
        StdOut.println("Average completion time: " + averageCompletionTime);
    }
}
