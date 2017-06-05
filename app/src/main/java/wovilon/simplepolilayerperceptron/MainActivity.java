package wovilon.simplepolilayerperceptron;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    double[] enters;
    double[] hidden;
    double outer;
    double [][] nEH; //neurons between enter layer and hidden layer
    double[] nHO; //neurons between hidden layer and outer layer
    double[][] patterns={
            {0,0}, {1,0}, {0,1}, {1,1}
    };
    double[] answers= {0,1,1,0};

    void runCalculation(){
        enters=new double[patterns[0].length];
        hidden=new double[2];
        nEH=new double[enters.length][hidden.length];
        nHO=new double[hidden.length];

        initWeights();
        study();
        for (int p=0; p<patterns.length; p++) {
            for (int i = 0; i < enters.length; i++)
                enters[i] = patterns[p][i];
            countOuter();
            Log.d("MyLOG", outer+"");
        }


    }

    //init default values
    public void initWeights(){
        for(int i=0; i<enters.length; i++){
            for (int j=0; j<nEH[i].length; j++){
                nEH[i][j]=Math.random()*0.2+0.1;
            }
        }

        for (int i=0; i<nHO.length; i++){
            nHO[i]=Math.random()*0.2+0.1;
        }
    }

    public void countOuter(){
        for(int i=0; i<hidden.length; i++){
            hidden[i]=0;
            for(int j=0; j<enters.length; j++){
            hidden[i]+=enters[j]*nEH[j][i];
            }
            if(hidden[i]>0.5 )hidden[i]=1; else hidden[i]=0;
        }

        outer=0;
        for (int i=0; i<hidden.length; i++){
            outer+=hidden[i]*nHO[i];
        }
        if (outer>0.5) outer=1; else outer=0;
    }

    public void study(){
        double[] err=new double[hidden.length];
        //calculate errors on all layers
        double gError=0;
        do {
            gError=0;
            for (int p=0; p<patterns.length; p++){
            for (int i = 0; i < enters.length; i++)
                enters[i]=patterns[p][i];

                countOuter();

                double localError=answers[p]-outer;
                gError+=Math.abs(localError);

                for (int i=0; i<hidden.length; i++){
                    err[i]=localError*nHO[i];
                }

                //correct weights
                for (int i=0; i<enters.length; i++){
                    for (int j=0; j<hidden.length; j++){
                        nEH[i][j]+=0.1*err[j]*enters[i];
                    }
                }

                for (int i=0; i<hidden.length; i++)
                    nHO[i]+=0.1*localError*hidden[i];

            }

        }while (gError!=0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runCalculation();
    }
}
