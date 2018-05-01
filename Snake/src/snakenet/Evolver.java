/*
 * 
 */
package snakenet;

import java.util.Arrays;
import java.util.Random;
import snake.*;

/**
 *
 * @author pmaclean
 */
public class Evolver {

    double mr;
    int popS;
    int keep;
    NeuralNet[] population;
    NeuralNet[] kept;
    Random rand;
    int size;

    public Evolver(int pop, int k, double rate, int gSize) {
        popS = pop;
        population = new NeuralNet[pop];

        mr = rate;
        for (int i = 0; i < pop; i++) {
            population[i] = new NeuralNet(24, 16, 4, i);
        }
        kept = new NeuralNet[keep];
        keep = k;
        rand = new Random(System.currentTimeMillis());
        size = gSize;
    }

    public static long fitness(NeuralNet n) {
        int score, time;
        try{
            score = n.score / n.testCount;
            time = n.ticks / n.testCount;
        } catch(Exception e){
            score = 0;
            time = 0;
        }
        return score<10?time*time*(long)Math.pow(2,score):1024*(score-9)*time*time;
    }

    public static NeuralNet[] sortByFitness(NeuralNet[] a) {
        boolean work = false;
        do {
            work = false;
            for (int i = 0; i < a.length - 1; i++) {
                if (fitness(a[i]) < fitness(a[i + 1])) {
                    work = true;
                    NeuralNet temp = a[i].clone();
                    a[i] = a[i + 1].clone();
                    a[i + 1] = temp;
                }
            }
        } while (work);
        System.out.println(fitness(a[0]));
        return a;
    }

    public static String artf(NeuralNet[] in) {
        String[] out = new String[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = "" + fitness(in[i]);
        }
        return Arrays.toString(out);
    }

    public static NeuralNet[] testNets(NeuralNet[] na, int size) {
        Game[] games = new Game[na.length];
        boolean anyAlive = false;
        int[] stopCount = new int[na.length];
        int[] scoreTrack = new int[na.length];
        for (int j = 0; j < na.length; j++) {
            games[j] = new Game(size, size, j);
            stopCount[j] = 0;
            scoreTrack[j] = na[j].score;
        }
        do {
            anyAlive = false;
            for (int j = 0; j < na.length; j++) {
                GameState ref = games[j].getState();
                if(ref.score!=scoreTrack[j]){
                    stopCount[j] = 0;
                    scoreTrack[j]=ref.score;
                } else {
                    stopCount[j]++;
                }
                if (ref.alive&&stopCount[j]<=500) {
                    anyAlive = true;
                    int move = na[j].move(ref);
                    //System.out.println(j+", "+na[j].ticks + ", "+ move);
                    games[j].tick(move);
                } else {
                        ref.alive = false;
                        na[j].fin(ref);                    
                }
            }
        } while (anyAlive);
        /*for(Game g : games){
            String[] bs = g.getState().board.toString().split("], ");        
            for(String s : bs){
                System.out.println(s);
            }
        }*/
        return na;
    }
    
    public NeuralNet selectNet(NeuralNet[] a, long total){        
        long runSum = 0;
        long cap = (long) (rand.nextDouble()*total);
        for(int i=0;i<a.length;i++){
            runSum += fitness(a[i]);
            if(runSum>cap){
                return a[i];
            }
        }
        return a[0];
    }
    
    public NeuralNet evolve(int gens) {
        NeuralNet best = new NeuralNet(1,1,1,1);
        for (int i = 0; i < gens; i++) {
            testNets(population, size);
            testNets(population, size);
            testNets(population, size);
            long totalFitness = 0;
            int bIndex = 0;
            
            NeuralNet[] newPop = new NeuralNet[popS];
            for(int j=0;j<population.length;j++){
                long fit = fitness(population[j]);
                totalFitness += fit;
                if(fit>fitness(population[bIndex])) bIndex = j;
            }
            if(fitness(population[bIndex])>fitness(best)) best = population[bIndex];
            
            if(i%50==0) {                
                System.out.println("generation: "+ i + ", best fitness: " +fitness(best));
            }
            for(int j=0;j<popS;j++){
                NeuralNet par1 = selectNet(population, totalFitness);
                NeuralNet par2 = selectNet(population, totalFitness);
                NeuralNet child = par1.crossover(par2);
                child.mutate(mr);
                newPop[j]=child;                
            }
            population = newPop;
        }
        testNets(population, size);
        testNets(population, size);
        testNets(population, size);
        int bIndex = 0;
        for(int j=0;j<population.length;j++){
                long fit = fitness(population[j]);
                if(fit>fitness(population[bIndex])) bIndex = j;
        }
        System.out.println(fitness(population[bIndex]));
        return population[bIndex];
    }
}

//old genetic algorithm
/*
            testNets(population, size);
            NeuralNet[] sorted = sortByFitness(population);
            if(fitness(sorted[0])>fitness(best)){
                best = sorted[0];
            }
            //System.out.println(artf(sorted));
            kept = new NeuralNet[keep];
            for (int k = 0; k < keep; k++) {
                NeuralNet temp = sorted[k].clone();
                kept[k] = temp.clone();
            }
            //System.out.println(artf(kept));
            for (int k = 0; k < popS; k++) {
                int mom = rand.nextInt(keep - 1);
                int dad;
                do {
                    dad = rand.nextInt(keep - 1);
                } while (mom == dad);
                population[k] = kept[mom].crossover(kept[dad]);
                population[k].mutate(mr);
            }*/
