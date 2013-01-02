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
package de.knewcleus.openradar.gui.status;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JTextField;

import de.knewcleus.fgfs.location.Vector2D;
import de.knewcleus.fgfs.navdata.model.INavPoint;
import de.knewcleus.openradar.gui.GuiMasterController;
import de.knewcleus.openradar.gui.contacts.GuiRadarContact;
import de.knewcleus.openradar.gui.status.runways.RunwayPanel;
import de.knewcleus.openradar.gui.status.runways.RunwaySettingsDialog;
import de.knewcleus.openradar.view.Converter2D;
import de.knewcleus.openradar.view.map.IMapViewerAdapter;
import de.knewcleus.openradar.view.navdata.INavPointListener;

/**
 * The controller for the status info panel
 * Main task of this class is to update the information displayed as "selection to pointer", direction to and from mousepointer, distance and time needed
 * 
 * @author Wolfram Wagner
 *
 */
public class StatusManager implements INavPointListener {

    private GuiMasterController master;
    private StatusPanel statusPanel = null;
//    private DetailPanel detailPanel = null;
    private CallSignActionListener callSignActionListener = new CallSignActionListener();
    private CallSignKeyListener callSignKeyListener = new CallSignKeyListener();
    private RunwayMouseListener runwayMouseListener = new RunwayMouseListener();    

    private RunwaySettingsDialog settingDialog;
    
    public StatusManager(GuiMasterController guiInteractionManager) {
        this.master = guiInteractionManager;
        this.settingDialog = new RunwaySettingsDialog(master);
    }
    
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel=statusPanel;
    }
//    public void setDetailPanel(DetailPanel detailPanel) {
//        this.detailPanel=detailPanel;
//    }
    
    public void setSelectedCallSign(String callsign) { 
        this.statusPanel.setAirport(master.getDataRegistry().getAirportCode()+" / "+ (master.getDataRegistry().getAirportName()));
        statusPanel.setSelectedCallSign(callsign);
    }
    
    public void updateTime() {
        statusPanel.updateTime();
    }


    @Override
    public void navPointAdded(INavPoint point) {
//        if(point instanceof )
        
    }

    public void updateMouseRadarMoved(GuiRadarContact contact, MouseEvent e) {
        IMapViewerAdapter mapViewerAdapter = contact.getMapViewerAdapter();
        double milesPerHour = contact.getAirSpeedD(); 
        Point2D currSelectionPoint = contact.getCenterViewCoordinates();

        double dx = e.getX()-currSelectionPoint.getX();
        double dy = currSelectionPoint.getY()-e.getY();
        Vector2D vDistance = new Vector2D(dx, dy);
        double distance = vDistance.getLength();
        Double angle = vDistance.getAngle();
        // angle corrections
        // 1. magnetic
        angle = angle + Math.round(master.getDataRegistry().getMagneticDeclination());
        // 2. wind
        Vector2D vOriginalAngle = Vector2D.createScreenVector2D(angle,contact.getAirSpeedD());
        Vector2D vWind = Vector2D.createVector2D((double)90-master.getMetar().getWindDirection(),master.getMetar().getWindSpeed());
        Vector2D vResult = vOriginalAngle.add(vWind);
        Long lAngle = vResult.getAngleL();
        
        Long degreesToPointer = lAngle!=null ? ( lAngle<0 ? lAngle+360 : lAngle) : null;
        Long degreesToSelection = lAngle!=null ? (degreesToPointer<180 ? degreesToPointer+180 : degreesToPointer-180) : null;
        // distances
        Double distanceMiles = distance*Converter2D.getMilesPerDot(mapViewerAdapter);
        Long timeMinutes = milesPerHour>10 ? Math.round(60*distanceMiles/(double)milesPerHour) : null;
        boolean hasChanged = true;
        //System.out.println("orig "+angle+" vOA: "+vOriginalAngle.getAngleL()+" vW "+vWind.getAngleL()+" result "+lAngle);
        if(hasChanged)statusPanel.setSelectionToPointer(degreesToPointer,degreesToSelection,distanceMiles, timeMinutes);
    }
 
    public CallSignActionListener getCallSignActionListener() {
        return callSignActionListener;
    }
    
    private class CallSignActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            master.setCurrentATCCallSign(statusPanel.getCurrentCallSign());
        }
    }

    public KeyListener getCallSignKeyListener() {
        return callSignKeyListener;
   }

    private class CallSignKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            JTextField tfSource = (JTextField)e.getSource();
            if(tfSource.getText().length()>6 
               && !(tfSource.getSelectionEnd()-tfSource.getSelectionStart()>0)) {
                tfSource.setText(tfSource.getText().substring(0,7));
                e.consume();
            }
        }
    }

    public MouseListener getRunwayMouseListener() {
        return runwayMouseListener;
    }

    private class RunwayMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2 && e.getSource() instanceof RunwayPanel) {
                ((RunwayPanel)e.getSource()).toggleActiveRunwayVisibility();
            }
            if(e.getButton()==MouseEvent.BUTTON3 && e.getClickCount()==1 && e.getSource() instanceof JLabel) {
                String rwCode = ((JLabel)e.getSource()).getText();
                if(rwCode!=null) {
                    settingDialog.setLocation(e);
                    settingDialog.showData(rwCode);
                }
            }
        }
    }

//    public void requestFocusForDetailInput() {
//        detailPanel.requestFocusForDetailInput();
//    }
//
    public String getActiveRunways() {
        
        return statusPanel.getActiveRunways();
    }

    public void hideRunwayDialog() {
        settingDialog.setVisible(false);
    }
}