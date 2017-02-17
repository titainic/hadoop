package spark.ml.kmeans;

import javax.swing.*;
import java.awt.*;

/**
 * Created by titanic on 17-2-17.
 */

public class GraphExample extends JFrame
{
    Mypanel mypanel = null;

    public static void main(String args[])
    {

        GraphExample a = new GraphExample();
    }

    public GraphExample()
    {
        mypanel = new Mypanel();
        this.add(mypanel);
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    class Mypanel extends JPanel
    {
        public void paint(Graphics g)
        {
            g.drawOval(30, 30, 50, 50);//30,30是代表圆心的位置。50，50是半径。（如果你把50，50改成2个不相等的就是话椭圆），具体画别的图形你可以参照Graphics类，其中用的多的还有drawLine（画直线）和drawRect（画矩形）的方法
        }
    }

}
