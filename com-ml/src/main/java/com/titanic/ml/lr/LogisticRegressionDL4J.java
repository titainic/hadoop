package com.titanic.ml.lr;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static org.nd4j.linalg.ops.transforms.Transforms.abs;
import static org.nd4j.linalg.ops.transforms.Transforms.exp;

public class LogisticRegressionDL4J
{
    private static INDArray sigmoid(INDArray Z)
    {
        //Z = (-Z)
        Z = Z.mul(-1.0);
        //Z = e^(Z);
        Z = exp(Z, false);
        //1 + Z
        Z = Z.add(1.0);

        //反向除法的缩写
        //1.0 / Z
        Z = Z.rdiv(1.0);
        return Z;
    }

    //
    private static INDArray calculateOutput(INDArray X, INDArray theta)
    {
        INDArray z = X.mmul(theta);
        return sigmoid(z);
    }


    private static INDArray gradientFunction(INDArray theta, INDArray X, INDArray y)
    {

        //number of samples
        int m = (int) X.size(0);

        INDArray h = calculateOutput(X, theta);
        // difference between predicted and actual class
        INDArray diff = h.dup().sub(y);
        return X.dup().transpose().mmul(diff).mul(1.0 / (double) m);
    }

    private static INDArray training(double alpha, INDArray X, INDArray y, int maxIterations, double epsilon)
    {
        //set random seed
        Nd4j.getRandom().setSeed(1234);
        //initialize theta randomly
        INDArray theta = Nd4j.rand((int) X.size(1), 1);

        INDArray newTheta = theta.dup();

        INDArray optimalTheta = theta.dup();
        for (int i = 0; i < maxIterations; i++)
        {
            INDArray gradients = gradientFunction(theta, X, y);

            //calculate new theta with gradients and learning rate alpha
            gradients = gradients.mul(alpha);
            newTheta = theta.sub(gradients);

            if (hasConverged(theta, newTheta, epsilon))
            {
                System.out.println("Done");
                break;
            }
            theta = newTheta;

        }
        optimalTheta = newTheta;

        return optimalTheta;
    }

    private static boolean hasConverged(INDArray oldTheta, INDArray newTheta, double epsilon)
    {
        double diffSum = abs(oldTheta.sub(newTheta)).sumNumber().doubleValue();
        return diffSum / (double) oldTheta.size(0) < epsilon;
    }
}
