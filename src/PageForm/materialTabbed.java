
package PageForm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class materialTabbed extends JTabbedPane{
  
    public materialTabbed(){
        setUI(new materialTabbedUI());
       
   } 

    
    public class materialTabbedUI extends MetalTabbedPaneUI{

        /**
         * @param currentRectangle the currentRectangle to set
         */
        public void setCurrentRectangle(Rectangle currentRectangle) {
            this.currentRectangle = currentRectangle;
            repaint();
        }
        
        private Animator animator;
        private Rectangle currentRectangle;
        private TimingTarget target;
        
        public materialTabbedUI(){
             }

        @Override
        public void installUI(JComponent jc) {
            super.installUI(jc); 
            animator=new Animator(500);
            animator.setResolution(0);
            animator.setAcceleration(.5f);
            animator.setDeceleration(.5f);
            tabPane.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent ce) {
                    int selected=tabPane.getSelectedIndex();
                    if (selected != -1){
                        if(currentRectangle !=null){
                            if(animator.isRunning()){
                                animator.stop();
                            }
                            animator.removeTarget(target);
                            target = new PropertySetter(materialTabbedUI.this,"currentRectangle",currentRectangle, getTabBounds(selected, calcRect));
                            animator.addTarget(target);
                            animator.start();
                        }
                    }
                }
                
            });
            
        
        }

        @Override
        protected Insets getTabInsets(int i, int i1) {
            return new Insets(10, 10, 10, 10);
        }
        
        
        @Override
        protected void paintTabBorder(Graphics grphcs, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2=(Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(119,107,93));
            if(currentRectangle ==null || !animator.isRunning()){
                if(isSelected){
                    currentRectangle= new Rectangle(x, y, w, h);                   
                }
            }
            if(currentRectangle != null){
                g2.fillRect(currentRectangle.x, currentRectangle.y+currentRectangle.height-3, currentRectangle.width, 3);    
            }
            g2.dispose();
        }

        @Override
        protected void paintContentBorder(Graphics grphcs, int tabPlacement, int tabIndex) {
            
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200,200,200));
           Insets insets = getTabAreaInsets(tabPlacement);
           int width = tabPane.getWidth();
           int height = tabPane.getHeight();
            if (tabPlacement == TOP) {
                int tabHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                g2.drawLine(insets.left, tabHeight, width - insets.right - 1, tabHeight);
                
            } 
            g2.dispose();
        }
        
        @Override
        protected void paintFocusIndicator(Graphics grphcs, int i, Rectangle[] rctngls, int i1, Rectangle rctngl, Rectangle rctngl1, boolean bln) {
            
        }
        
    }

}
