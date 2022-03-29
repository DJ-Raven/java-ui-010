package javaswingdev.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javaswingdev.GoogleMaterialDesignIcon;
import javaswingdev.GoogleMaterialIcon;
import javaswingdev.GradientType;
import javaswingdev.event.EventMenu;
import javaswingdev.form.Test_Form;
import javaswingdev.swing.Button;
import javax.swing.JLayeredPane;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class LayerForm extends JLayeredPane {
    
    private boolean showing;
    private float animate;
    
    public LayerForm() {
        setBackground(new Color(242, 242, 242));
        layout = new MigLayout("fill,inset 3", "[fill]", "[fill]");
        setLayout(layout);
        //  Init menu
        menu = new PanelMenu();
        menu.addEvent(new EventMenu() {
            @Override
            public void menuSelected(int index) {
                form.showForm(new Test_Form("Form : " + index));
                setVisibleMenu(false);
            }
        });
        add(menu, "pos 1al 0al, w 0!, h 0!");
        //  Init form
        form = new Form();
        add(form);
        //  Init button menu
        buttonMenu = new Button();
        buttonMenu.setIcon(new GoogleMaterialIcon(GoogleMaterialDesignIcon.DEHAZE, GradientType.VERTICAL, new Color(255, 255, 255), new Color(132, 176, 255), 25).toIcon());
        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisibleMenu(!showing);
            }
        });
        setLayer(buttonMenu, JLayeredPane.POPUP_LAYER);
        add(buttonMenu, "pos 1al 0al, h 40, w 40", 0);
        //  Init animator
        animator = new Animator(350, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = showing ? fraction : 1f - fraction;
                int w = (int) (animate * 100);
                int h = (int) (animate * 100);
                layout.setComponentConstraints(menu, "pos 1al 0al, w " + w + "%-6!, h " + h + "%-6!");
                revalidate();
                menu.setAnimate(animate);
            }
        });
        animator.setResolution(0);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        form.showForm(new Test_Form("Init form"));
    }
    
    private void startAnimator(boolean showing) {
        if (animator.isRunning()) {
            animator.stop();
            float f = animator.getTimingFraction();
            animator.setStartFraction(1f - f);
        } else {
            animator.setStartFraction(0f);
        }
        this.showing = showing;
        animator.start();
    }
    
    public void setVisibleMenu(boolean show) {
        if (show != showing) {
            startAnimator(show);
            if (showing) {
                buttonMenu.setIcon(new GoogleMaterialIcon(GoogleMaterialDesignIcon.CLEAR, GradientType.VERTICAL, new Color(255, 255, 255), new Color(132, 176, 255), 25).toIcon());
            } else {
                buttonMenu.setIcon(new GoogleMaterialIcon(GoogleMaterialDesignIcon.DEHAZE, GradientType.VERTICAL, new Color(255, 255, 255), new Color(132, 176, 255), 25).toIcon());
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
    
    private Animator animator;
    private MigLayout layout;
    private PanelMenu menu;
    private Button buttonMenu;
    private Form form;
}
