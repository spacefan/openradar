/**
 * Copyright (C) 2012 Wolfram Wagner 
 * 
 * This file is part of OpenRadar.
 * 
 * OpenRadar is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OpenRadar is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OpenRadar. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Diese Datei ist Teil von OpenRadar.
 * 
 * OpenRadar ist Freie Software: Sie können es unter den Bedingungen der GNU
 * General Public License, wie von der Free Software Foundation, Version 3 der
 * Lizenz oder (nach Ihrer Option) jeder späteren veröffentlichten Version,
 * weiterverbreiten und/oder modifizieren.
 * 
 * OpenRadar wird in der Hoffnung, dass es nützlich sein wird, aber OHNE JEDE
 * GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite Gewährleistung der
 * MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK. Siehe die GNU General
 * Public License für weitere Details.
 * 
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package de.knewcleus.openradar.gui.contacts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import de.knewcleus.openradar.gui.GuiMasterController;

/**
 * This class renders the flight strip like radar contacts 
 * 
 * @author Wolfram Wagner
 */

public class RadarContactListCellRenderer extends JComponent implements ListCellRenderer<GuiRadarContact> {
    private static final long serialVersionUID = 4683696532302543565L;

    private GuiMasterController master;
    
    private JPanel spaceLEFT = null;
    private JPanel strip = null;
    private JLabel lbFrequency = null;
    private JLabel lbCallSign = null;
    private JLabel lbRadarDistance = null;
    private JLabel lbRadarBearing = null;
    private JLabel lbHeading = null;
    private JLabel lbAircraft = null;
    private JLabel lbFlightLevel= null;
    private JLabel lbTrueSpeed = null;
    private JLabel lbGroundSpeed = null;
//    private JLabel lbVerticalSpeed = null;
    private JTextArea taAtcComment = null;
    private JPanel lowerArea = null;

    private GridBagConstraints stripConstraints;
    private GridBagConstraints spaceRightConstraints;
    private GridBagConstraints lbFrequencyConstraints;
    private GridBagConstraints lbCallSignConstraints;
    private GridBagConstraints lbRadarDistanceConstraints;
    private GridBagConstraints lbRadarBearingConstraints;
    private GridBagConstraints lbHeadingConstraints;
    private GridBagConstraints lbAircraftConstraints;
    private GridBagConstraints lbFlightLevelConstraints;
    private GridBagConstraints lbTrueSpeedConstraints;
    private GridBagConstraints lbGroundSpeedConstraints;
//    private GridBagConstraints lbVerticalSpeedConstraints;
    private GridBagConstraints taAtcCommentConstraints;
    
//    private Font defaultFont = new java.awt.Font("Cantarell", Font.PLAIN, 12); // NOI18N
//    private Font smallFont = new java.awt.Font("Cantarell", Font.PLAIN, 9); // NOI18N
//    private Font boldFont = new java.awt.Font("Cantarell", Font.BOLD, 12); // NOI18N

    private Font defaultFont;
    private Font smallFont;
    private Font boldFont;

    private Color defaultColor = Color.BLACK;
    private Color incativeColor = Color.GRAY;
    private Color selectionColor = Color.BLUE;
    private Color emergencyColor = Color.RED;

    public static int STRIP_WITDH = 250;

    
    public RadarContactListCellRenderer(GuiMasterController master) {
        this.master=master;
        this.setLayout(new GridBagLayout());

        spaceLEFT = new JPanel();
        spaceLEFT.setOpaque(false);
        spaceRightConstraints = new GridBagConstraints();
        spaceRightConstraints.gridx = 0;
        spaceRightConstraints.gridy = 0;
        spaceRightConstraints.gridheight = 1;
        spaceRightConstraints.fill=GridBagConstraints.HORIZONTAL;
        spaceRightConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(spaceLEFT, spaceRightConstraints);
        
        strip = new JPanel();
        strip.setLayout(new GridBagLayout());
        stripConstraints = new GridBagConstraints();
        stripConstraints.gridx = 1;
        stripConstraints.gridy = 0;
        stripConstraints.gridheight = 1;
        stripConstraints.weightx=1;
        stripConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        stripConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(strip, stripConstraints);

        // first line
        
        lbCallSign = new JLabel();
        defaultFont = lbCallSign.getFont().deriveFont(10f);
        smallFont = lbCallSign.getFont().deriveFont(9f);
        boldFont = lbCallSign.getFont().deriveFont(10f).deriveFont(Font.BOLD);

        lbCallSign.setFont(boldFont);
        lbCallSign.setForeground(java.awt.Color.blue);
        lbCallSign.setToolTipText("Current call sign");
        lbCallSign.setMinimumSize(new Dimension(60,defaultFont.getSize()+2));
        lbCallSign.setPreferredSize(new Dimension(60,defaultFont.getSize()+2));
        lbCallSign.setOpaque(true);
        lbCallSignConstraints = new GridBagConstraints();
        lbCallSignConstraints.gridx = 0;
        lbCallSignConstraints.gridy = 0;
        lbCallSignConstraints.gridwidth = 1;
        lbCallSignConstraints.weightx=1;
        lbCallSignConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        lbCallSignConstraints.insets = new java.awt.Insets(4, 4, 0, 5);
        strip.add(lbCallSign, lbCallSignConstraints);

        lbAircraft = new JLabel();
        lbAircraft.setFont(defaultFont);
        lbAircraft.setForeground(java.awt.Color.blue);
        lbAircraft.setOpaque(true);
        lbAircraft.setMinimumSize(new Dimension(80,defaultFont.getSize()+2));
        lbAircraft.setPreferredSize(new Dimension(80,defaultFont.getSize()+2));
        lbAircraftConstraints = new GridBagConstraints();
        lbAircraftConstraints.gridx = 1;
        lbAircraftConstraints.gridy = 0;
        lbAircraftConstraints.weightx=1;
        lbAircraftConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        lbAircraftConstraints.insets = new java.awt.Insets(4, 4, 0, 5);
        strip.add(lbAircraft, lbAircraftConstraints);

        lbRadarBearing = new JLabel();
        lbRadarBearing.setFont(defaultFont);
        lbRadarBearing.setForeground(java.awt.Color.blue);
        lbRadarBearing.setPreferredSize(new Dimension(160,defaultFont.getSize()+2));
        lbRadarBearing.setOpaque(true);
        lbRadarBearingConstraints = new GridBagConstraints();
        lbRadarBearingConstraints.gridx = 2;
        lbRadarBearingConstraints.gridy = 0;
        lbRadarBearingConstraints.gridwidth = 1;
        lbRadarBearingConstraints.weightx=0;
        lbRadarBearingConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        lbRadarBearingConstraints.insets = new java.awt.Insets(4, 4, 0, 5);
        strip.add(lbRadarBearing, lbRadarBearingConstraints);
        
        lbFrequency = new JLabel();
        lbFrequency.setFont(defaultFont);
        lbFrequency.setForeground(java.awt.Color.blue);
        lbFrequency.setPreferredSize(new Dimension(35,defaultFont.getSize()+2));
        lbFrequency.setOpaque(true);
        lbFrequencyConstraints = new GridBagConstraints();
        lbFrequencyConstraints.gridx = 3;
        lbFrequencyConstraints.gridy = 0;
        lbFrequencyConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        lbFrequencyConstraints.insets = new java.awt.Insets(4,0, 0, 5);
        strip.add(lbFrequency, lbFrequencyConstraints);

        // second line

        lowerArea = new JPanel();
        lowerArea.setLayout(new GridBagLayout());
        lowerArea.setBackground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth=4;
        constraints.gridheight=1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill=GridBagConstraints.BOTH;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 0, 0, 0);
        strip.add(lowerArea, constraints);

        lbRadarDistance = new JLabel();
        lbRadarDistance.setFont(boldFont);
        lbRadarDistance.setForeground(java.awt.Color.blue);
        lbRadarDistance.setMinimumSize(new Dimension(50,defaultFont.getSize()+2));
        lbRadarDistance.setPreferredSize(new Dimension(50,defaultFont.getSize()+2));
        lbRadarDistance.setOpaque(true);
        lbRadarDistanceConstraints = new GridBagConstraints();
        lbRadarDistanceConstraints.gridx = 0;
        lbRadarDistanceConstraints.gridy = 0;
        lbRadarDistanceConstraints.anchor = java.awt.GridBagConstraints.EAST;
        lbRadarDistanceConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        lowerArea.add(lbRadarDistance, lbRadarDistanceConstraints);
        
        lbTrueSpeed = new JLabel();
        lbTrueSpeed.setFont(boldFont);
        lbTrueSpeed.setForeground(java.awt.Color.blue);
        lbTrueSpeedConstraints = new GridBagConstraints();
        lbTrueSpeedConstraints.gridx = 1;
        lbTrueSpeedConstraints.gridy = 0;
        lbTrueSpeedConstraints.anchor = java.awt.GridBagConstraints.WEST;
        lbTrueSpeedConstraints.insets = new java.awt.Insets(0, 40, 4, 0);
        lowerArea.add(lbTrueSpeed, lbTrueSpeedConstraints);
        
        lbGroundSpeed = new JLabel();
        lbGroundSpeed.setFont(defaultFont);
        lbGroundSpeed.setForeground(java.awt.Color.blue);
        lbGroundSpeed.setOpaque(true);
        lbGroundSpeedConstraints = new GridBagConstraints();
        lbGroundSpeedConstraints.gridx = 2;
        lbGroundSpeedConstraints.gridy = 0;
        lbGroundSpeedConstraints.weightx = 0;
        lbGroundSpeedConstraints.anchor = java.awt.GridBagConstraints.WEST;
        lbGroundSpeedConstraints.insets = new java.awt.Insets(0, 8, 4, 0);
        lowerArea.add(lbGroundSpeed, lbGroundSpeedConstraints);

        
        lbFlightLevel = new JLabel();
        lbFlightLevel.setFont(boldFont);
        lbFlightLevel.setForeground(java.awt.Color.blue);
//        lbFlightLevel.setPreferredSize(new Dimension(45,defaultFont.getSize()+2));
        lbFlightLevel.setOpaque(true);
        lbFlightLevelConstraints = new GridBagConstraints();
        lbFlightLevelConstraints.gridx = 3;
        lbFlightLevelConstraints.gridy = 0;
        lbFlightLevelConstraints.anchor = java.awt.GridBagConstraints.EAST;
        lbFlightLevelConstraints.insets = new java.awt.Insets(0, 25, 4, 5);
        lowerArea.add(lbFlightLevel, lbFlightLevelConstraints);

        lbHeading = new JLabel(" ");
        lbHeading.setFont(boldFont);
        lbHeading.setForeground(java.awt.Color.blue);
        lbHeading.setOpaque(true);
        lbHeadingConstraints = new GridBagConstraints();
        lbHeadingConstraints.gridx = 4;
        lbHeadingConstraints.gridy = 0;
        lbHeadingConstraints.weightx = 1;
        lbHeadingConstraints.fill = GridBagConstraints.HORIZONTAL;
        lbHeadingConstraints.anchor = java.awt.GridBagConstraints.EAST;
        lbHeadingConstraints.insets = new java.awt.Insets(0, 5, 4, 5);
        lowerArea.add(lbHeading, lbHeadingConstraints);
        

        // third line
        
        taAtcComment = new JTextArea();
        taAtcComment.setFont(smallFont);
        taAtcComment.setForeground(java.awt.Color.blue);
        taAtcComment.setOpaque(true);
        taAtcComment.setEditable(false);
        taAtcCommentConstraints = new GridBagConstraints();
        taAtcCommentConstraints.gridx = 0;
        taAtcCommentConstraints.gridy = 1;
        taAtcCommentConstraints.gridwidth=5;
        taAtcCommentConstraints.gridheight=1;
        taAtcCommentConstraints.weightx = 1;
        taAtcCommentConstraints.weighty = 1;
        taAtcCommentConstraints.fill=GridBagConstraints.BOTH;
        taAtcCommentConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        taAtcCommentConstraints.insets = new java.awt.Insets(0, 4, 6, 4);
        lowerArea.add(taAtcComment, taAtcCommentConstraints);
//
//        lbVerticalSpeed = new JLabel();
//        lbVerticalSpeed.setFont(defaultFont);
//        lbVerticalSpeed.setForeground(java.awt.Color.blue);
//        lbVerticalSpeed.setOpaque(true);
//        lbVerticalSpeedConstraints = new GridBagConstraints();
//        lbVerticalSpeedConstraints.gridx = 2;
//        lbVerticalSpeedConstraints.gridy = 1;
//        lbVerticalSpeedConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
//        lbVerticalSpeedConstraints.insets = new java.awt.Insets(0, 0, 6, 5);
//        lowerArea.add(lbVerticalSpeed, lbVerticalSpeedConstraints);
        
        doLayout();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends GuiRadarContact> list, GuiRadarContact value, int index, boolean isSelected, boolean cellHasFocus) {
        int totalWidth = list.getParent().getWidth();
        int moveIncrement = (totalWidth-STRIP_WITDH)/2-4;
        
        // alinment
        switch(value.getAlignment()) {
        case LEFT:
            spaceLEFT.setPreferredSize(new Dimension(0,0));
            break;
        case CENTER:
            spaceLEFT.setPreferredSize(new Dimension(moveIncrement*1,0));
            break;
        case RIGHT:
            spaceLEFT.setPreferredSize(new Dimension(moveIncrement*2,0));
            break;
        }

        lbCallSign.setText(value.getCallSign());
        lbFlightLevel.setText(value.getFlightLevel());
        String atcFrequencyList = master.getRadioManager().getActiveFrequencyList();
        if(value.getFrequency()!=null) {
            if (atcFrequencyList!=null && atcFrequencyList.contains(value.getFrequency())) {        
                lbFrequency.setText(value.getFrequency());
                lbFrequency.setForeground(Color.blue);
            } else {
                lbFrequency.setText("");
                lbFrequency.setForeground(Color.black);
            }
        }
        lbRadarDistance.setText(value.getRadarContactDistance());
        lbRadarBearing.setText("@"+value.getRadarContactDirection()+"°");
        lbHeading.setText(value.getMagnCourse());
        lbAircraft.setText((!value.isNeglect() ? value.getAircraft() : "(neglected)"));
        taAtcComment.setText(value.getAtcComment());
        lbTrueSpeed.setText(value.getAirSpeed());
        lbGroundSpeed.setText(value.getGroundSpeed());
        //lbVerticalSpeed.setText("V:"+value.getVerticalSpeed());

        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

            background = Color.WHITE;
            foreground = Color.WHITE;

            
        } else {
            // normal display
            
            foreground = defaultColor;
            background = Color.white;

            if (value.isOnEmergency()) {
                // font = activeFont;
                foreground = emergencyColor;
            } else if (value.isSelected()) {
                // font = activeFont;
                foreground = selectionColor;
            }
        }
        if(!value.isActive() || value.isNeglect()) {
            foreground=incativeColor;
        }
        
        // this.lbCallSign.setFont(activeFont);
        this.lbCallSign.setForeground(foreground);
        this.lbCallSign.setBackground(background);
        // this.lbFlightLevel.setFont(activeFont);
        this.lbFlightLevel.setForeground(foreground);
        this.lbFlightLevel.setBackground(background);
        // this.lbRadarDistance.setFont(font);
        this.lbRadarDistance.setForeground(foreground);
        this.lbRadarDistance.setBackground(background);
        // this.lbRadarBearing.setFont(font);
        this.lbRadarBearing.setForeground(foreground);
        this.lbRadarBearing.setBackground(background);
        // this.lbOperation.setFont(font);
        this.lbFrequency.setForeground(foreground);
        this.lbFrequency.setBackground(background);
        
        // this.lbHeading.setFont(activeFont);
        this.lbHeading.setForeground(foreground);
        this.lbHeading.setBackground(background);
        // this.lbAircraft.setFont(activeFont);
        this.lbAircraft.setForeground(foreground);
        this.lbAircraft.setBackground(background);
        this.taAtcComment.setForeground(foreground);
        this.taAtcComment.setBackground(background);

        this.lbTrueSpeed.setForeground(foreground);
        this.lbTrueSpeed.setBackground(background);
        // this.lbGroundSpeed.setFont(activeFont);
        this.lbGroundSpeed.setForeground(foreground);
        this.lbGroundSpeed.setBackground(background);

//        this.lbVerticalSpeed.setForeground(foreground);
//        this.lbVerticalSpeed.setBackground(background);

        boolean isAtcCommentEmpty = taAtcComment.getText().trim().isEmpty();
        
        int taNormalHeight = (int)lbCallSign.getPreferredSize().getHeight() + (int)lbTrueSpeed.getPreferredSize().getHeight() + 8;
        int taCommentHeight = isAtcCommentEmpty ? taNormalHeight : taNormalHeight + (int)taAtcComment.getPreferredSize().getHeight() + 8 ;
        strip.setPreferredSize(new Dimension(250,Math.max(taNormalHeight, taCommentHeight)));
        strip.setBackground(background);
        strip.setOpaque(true);
        
        return this;
    }
}
