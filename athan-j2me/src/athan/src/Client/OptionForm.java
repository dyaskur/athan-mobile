/*
 * Copyright © 2008, 2010, Oracle and/or its affiliates. All rights reserved
 */
package athan.src.Client;

import athan.src.Factory.ResourceReader;
import athan.src.Factory.ServiceFactory;
import athan.src.Factory.TacheTimer;
import athan.src.options.MenuCompass;
import athan.src.options.MenuAlerts;
import athan.src.options.MenuLocation;
import athan.src.options.MenuLocalTime;
import athan.src.options.MenuCalendarCorrection;
import athan.src.options.MenuApplicationLanguage;
import athan.src.options.MenuCalculationMethod;
import athan.src.options.MenuPrayers;
import athan.src.options.MenuSettingsSummary;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.animations.Animation;
import com.sun.lwuit.animations.Motion;
import com.sun.lwuit.animations.Transition;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.util.Hashtable;
import java.util.Timer;
import java.util.Vector;

/**
 * LWUIT Demo main Form, this special Form allows drag&drop on top of it. 
 * 
 * 
 * @author Chen Fishbein
 */
public class OptionForm extends Form
           implements ActionListener {
    
    private static final int EXIT_COMMAND = 1;
    private static final int BACK_COMMAND = 3;
    private static final int ABOUT_COMMAND = 4;
    /*
    private static final int RUN_COMMAND = 2;
    private static final int DRAG_MODE_COMMAND = 5;
    private static final int SCROLL_MODE_COMMAND = 6;
    private static final int RTL_COMMAND = 7;
    */

    private static final Command exitCommand = new Command("", EXIT_COMMAND);
    private static final Command backCommand = new Command("", BACK_COMMAND);
    private static final Command aboutCommand = new Command("", ABOUT_COMMAND);
    /*
    private static final Command runCommand = new Command("Run", RUN_COMMAND);
    private static final Command dragModeCommand = new Command("Drag Mode", DRAG_MODE_COMMAND);
    private static final Command scrollModeCommand = new Command("Scroll Mode", SCROLL_MODE_COMMAND);
    private static final Command rtlCommand = new Command("RTL", RTL_COMMAND);
    */

    private static Transition sComponentTransitions;

    private static final Menu[] DEMOS = new Menu[]{
        new MenuAlerts(),
        new MenuLocation(),
        new MenuCompass(),
        new MenuLocalTime(),
        new MenuCalendarCorrection(),
        new MenuApplicationLanguage(),
        new MenuCalculationMethod(),
        new MenuPrayers(),
        new MenuSettingsSummary()
    };

    private Hashtable mDemosHash = new Hashtable();
    private int mCols = 0;
    private int mElementWidth;

    private Component dragged;
    private int oldx;
    private int oldy;
    private int draggedx;
    private int draggedy;
    private Image draggedImage;
    private Vector cmps;
    private Transition cmpTransition;
    private MainForm parent;
    private boolean dragMode;
    private Menu mCurrentMenu;

    private ResourceReader RESOURCES;

    public static void setTransition(Transition in, Transition out) {
        /*
        if (Main.getOptionForm() != null) {
            Main.getMainForm().setTransitionInAnimator(in);
            Main.getMainForm().setTransitionOutAnimator(out);
        }
        */
    }

    public static void setMenuTransition(Transition in, Transition out) {
        /*
        if (Main.getOptionForm() != null) {
            Main.getMainForm().setMenuTransitions(in, out);
        }
        */
        UIManager.getInstance().getLookAndFeel().setDefaultMenuTransitionIn(in);
        UIManager.getInstance().getLookAndFeel().setDefaultMenuTransitionOut(out);
    }

    public static void setComponentTransition(Transition t) {
        if (t != null) {
            if (Main.getOptionForm() != null) {
                Main.getOptionForm().setSmoothScrolling(false);
            }
        }
        sComponentTransitions = t;
    }

    public static Transition getComponentTransition() {
        return sComponentTransitions;
    }

    public OptionForm(MainForm parent) {
        //setTitle(title);
        this.parent = parent;

        RESOURCES = ServiceFactory.getFactory().getResourceReader();
        setTitle(RESOURCES.get("OptionForm"));
    }
    
    public void setDragMode(boolean dragMode) {
        this.dragMode = dragMode;
        setSmoothScrolling(!dragMode);
    }
    
    protected void sizeChanged(int w, int h) {
        super.sizeChanged(w, h);
        try {
            this.parent.setOptionForm(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pointerDragged(int x, int y) {
        if(!dragMode) {
            super.pointerDragged(x, y);
            return;
        }
        if (draggedImage == null) {
            dragged = getComponentAt(x, y);
            if (dragged == null || !getContentPane().contains(dragged)) {
                super.pointerDragged(x, y);
                return;
            }
            draggedImage = Image.createImage(dragged.getWidth(), dragged.getHeight());
            Graphics g = draggedImage.getGraphics();
            g.setClip(0, 0, dragged.getWidth(), dragged.getHeight());
            //choose a rare color
            g.setColor(0xff7777);
            g.fillRect(0, 0, dragged.getWidth(), dragged.getHeight());
            g.translate(-dragged.getX(), -dragged.getY());
            dragged.paint(g);
            g.translate(dragged.getX(), dragged.getY());

            //remove all occurences of the rare color
            draggedImage = draggedImage.modifyAlpha((byte)0x55, 0xff7777);

            oldx = x;
            oldy = y;
            draggedx = dragged.getAbsoluteX();
            draggedy = dragged.getAbsoluteY();
            dragged.setVisible(false);
            Painter glassPane = new Painter() {
                public void paint(Graphics g, Rectangle rect) {
                    if (draggedImage != null) {
                        g.drawImage(draggedImage, draggedx, draggedy);
                    }
                }
            };
            setGlassPane(glassPane);
            cmpTransition = getComponentTransition();
            this.setComponentTransition(null);
            return;
        }

        repaint(draggedx, draggedy, dragged.getWidth(), dragged.getHeight());

        draggedx = draggedx + (x - oldx);
        draggedy = draggedy + (y - oldy);
        oldx = x;
        oldy = y;
        repaint(draggedx, draggedy, dragged.getWidth(), dragged.getHeight());
        super.pointerDragged(x, y);
    }

    public void pointerReleased(int x, int y) {
        if(!dragMode) {
            super.pointerReleased(x, y);
            return;
        }
        if (draggedImage == null) {
            super.pointerReleased(x, y);
            return;
        }
        setVisible(false);
        oldx = 0;
        oldy = 0;

        Component cmp = getFocused();

        final int index = getContentPane().getComponentIndex(cmp);
        cmps = new Vector();
        if (index >= 0) {
            int draggedIndex = getContentPane().getComponentIndex(dragged);
            int startIndex = Math.min(index, draggedIndex);
            for (int i = startIndex; i < getContentPane().getComponentCount(); i++) {
                Component toMove = getContentPane().getComponentAt(i);
                LayoutAnimation la = new LayoutAnimation(toMove);
                la.setFrom(new Dimension(toMove.getX(), toMove.getY()));
                cmps.addElement(la);
            }

            removeComponent(dragged);
            addComponent(index, dragged);

            layoutContainer();

            LayoutAnimation la = new LayoutAnimation(dragged);
            int dx= Math.max(0, draggedx - (dragged.getAbsoluteX() - dragged.getX()));
            int dy= Math.max(0, draggedy - (dragged.getAbsoluteY() - dragged.getY()));
            dx= Math.min(dx, getContentPane().getPreferredW() - dragged.getWidth());
            dy= Math.min(dy, getContentPane().getPreferredH() - dragged.getHeight());
            
            la.setFrom(new Dimension(dx, dy));
            la.setTo(new Dimension(dragged.getX(), dragged.getY()));
            la.init();

            for (int i = 0; i < cmps.size(); i++) {
                LayoutAnimation l = (LayoutAnimation) cmps.elementAt(i);
                l.setTo(new Dimension(l.toAnimate.getX(), l.toAnimate.getY()));
                l.init();
            }
            cmps.addElement(la);

            removeComponent(dragged);
            addComponent(draggedIndex, dragged);
            layoutContainer();
            la.init();
        } else {
            finishDrag();
            return;
        }

        registerAnimated(new Animation() {
            public boolean animate() {
                boolean retVal = false;
                for (int i = 0; i < cmps.size(); i++) {
                    LayoutAnimation la = (LayoutAnimation) cmps.elementAt(i);
                    if (la.animate()) {
                        retVal = true;
                    }
                }
                //if finished
                if (!retVal) {
                    deregisterAnimated(this);
                    if(getContentPane().contains(dragged)){
                        removeComponent(dragged);
                        addComponent(index, dragged);
                    }
                    finishDrag();
                }

                return retVal;
            }

            public void paint(Graphics g) {
                for (int i = 0; i < cmps.size(); i++) {
                    LayoutAnimation la = (LayoutAnimation) cmps.elementAt(i);
                    la.paint(g);
                    repaint(la.toAnimate.getAbsoluteX(), la.toAnimate.getAbsoluteY(),
                            la.toAnimate.getWidth(), la.toAnimate.getHeight());
                }
                if (!dragged.isVisible()) {
                    dragged.setVisible(true);
                }
            }
        });


        setVisible(true);
        repaint();
        setGlassPane(null);
    }

    private void finishDrag() {
        setGlassPane(null);
        if (dragged != null) {
            dragged.requestFocus();
            if (!dragged.isVisible()) {
                dragged.setVisible(true);
                dragged.requestFocus();
            }
            dragged = null;
        }
        draggedImage = null;
        this.setComponentTransition(cmpTransition);
        repaint();
    }

    /**
     * Invoked when a command is clicked. We could derive from Command but that would
     * require 3 separate classes.
     */
    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();
        switch (cmd.getId()) {
            case EXIT_COMMAND:
                Main.getMainForm().run(Main.exitCommand,
                        Main.getMainForm().getMain(),
                        false);
                Main.setTimer(new Timer());
                Main.getTimer().schedule(new TacheTimer(), 0, TacheTimer.DUREE_CYCLE);
                break;
            case BACK_COMMAND:
                mCurrentMenu.cleanup();
                this.show();
                break;
            case ABOUT_COMMAND:
                Form aboutForm = new Form(RESOURCES.get("Menu.About"));
                aboutForm.setScrollable(false);
                aboutForm.setLayout(new BorderLayout());
                TextArea aboutText = new TextArea(getAboutText(), 5, 10);
                aboutText.setEditable(false);
                aboutForm.addComponent(BorderLayout.CENTER, aboutText);
                final OptionForm self = this;
                aboutForm.addCommand(new Command(RESOURCES.get("Menu.Back")) {

                    public void actionPerformed(ActionEvent evt) {
                        self.show();
                    }
                });
                aboutForm.show();
                break;
        }
    }

    public void setMainForm(Resources pResource) {

        // application logic determins the number of columns based on the screen size
        // this is why we need to be aware of screen size changes which is currently
        // only received using this approach
        int width = Display.getInstance().getDisplayWidth(); //get the display width

        mElementWidth = 0;

        Image[] selectedImages = new Image[DEMOS.length];
        Image[] unselectedImages = new Image[DEMOS.length];

        final ButtonActionListener bAListner = new ButtonActionListener();
        for (int i = 0; i < DEMOS.length; i++) {
            selectedImages[i] = pResource.getImage(DEMOS[i].getIconBaseName() + "_sel");
            unselectedImages[i] = pResource.getImage(DEMOS[i].getIconBaseName() + "_unsel");

            Image imgSelectionnee = selectedImages[i];//.scaled(50, 50);
            Image imgDeselectionnee = unselectedImages[i];//.scaled(50, 50);
            imgDeselectionnee = imgSelectionnee;

            final Button b = new Button(DEMOS[i].getName(), imgDeselectionnee);
            b.setUIID(AthanConstantes.UIID_BUTTON_OPTIONS);
            b.setRolloverIcon(imgSelectionnee);
            b.setAlignment(Label.CENTER);
            b.setTextPosition(Label.BOTTOM);
            this.addComponent(b);
            b.addActionListener(bAListner);
            final OptionForm self = this ;
            b.addFocusListener(new FocusListener() {
                public void focusGained(Component cmp) {
                    if (sComponentTransitions != null) {
                        self.replace(b, b, sComponentTransitions);
                    }
                }

                public void focusLost(Component cmp) {
                }
            });

            mDemosHash.put(b, DEMOS[i]);
            mElementWidth = Math.max(b.getPreferredW(), mElementWidth);
        }

        //Calculate the number of columns for the GridLayout according to the
        //screen width
        if(mCols == 0) {
            mCols = width / mElementWidth;
        }
        int rows = DEMOS.length / mCols;
        if (rows == 0) {
            rows = 1;
        }

        this.setLayout(new GridLayout(rows, mCols));

        exitCommand.setCommandName(RESOURCES.get("Command.Back"));
        aboutCommand.setCommandName(RESOURCES.get("Command.About"));
        backCommand.setCommandName(RESOURCES.get("Command.Back"));
        
        this.addCommand(exitCommand);
        this.addCommand(aboutCommand);
        this.setBackCommand(exitCommand);
        this.addCommandListener(this);
    }

    private String getAboutText() {
        return RESOURCES.getContenu_About();
    }

    private class ButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            mCurrentMenu = ((Menu) (mDemosHash.get(evt.getSource())));
            mCurrentMenu.run(backCommand, OptionForm.this, true);
        }
    }

    class LayoutAnimation implements Animation {
        private Component toAnimate;
        private Dimension from;
        private Dimension to;
        private Motion xMotion;
        private Motion yMotion;

        LayoutAnimation(Component toAnimate) {
            this.toAnimate = toAnimate;
        }

        public void setFrom(Dimension from) {
            this.from = from;
        }

        public void setTo(Dimension to) {
            this.to = to;
        }

        public void init() {
            toAnimate.setX(from.getWidth());
            toAnimate.setY(from.getHeight());
            xMotion = Motion.createSplineMotion(from.getWidth(), to.getWidth(), 500);
            yMotion = Motion.createSplineMotion(from.getHeight(), to.getHeight(), 500);
            xMotion.start();
            yMotion.start();
        }

        public boolean animate() {
            toAnimate.setX(xMotion.getValue());
            toAnimate.setY(yMotion.getValue());
            return !xMotion.isFinished() && !yMotion.isFinished();
        }

        public void paint(Graphics g) {
            toAnimate.paintComponent(g);
        }
    }
}
