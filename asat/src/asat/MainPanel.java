package asat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jtalics.n3mo.Satellite;


public class MainPanel extends JPanel {

	private AppMain app;

	public MainPanel(AppMain app) {
		
		this.app=app;
		setLayout(new BorderLayout());
		SatelliteTable satTable = new SatelliteTable(app);
		add(satTable,BorderLayout.CENTER);
		SatelliteTableModel satTableModel = new SatelliteTableModel(app);
		satTable.setModel(satTableModel);
		JButton button = new JButton("Get latest keps");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					download();
					satTableModel.fireTableDataChanged();
				} catch (IOException e) {
					app.logger.log(Level.SEVERE,e.getMessage());
					// TODO notify user
				}
			}
		});
		add(button,BorderLayout.NORTH);
		
	};
	
	private void download() throws IOException {

		// TODO: confirm clearing of map
		app.satellites.clear();
		
		URL oracle = new URL("https://www.amsat.org/amsat/ftp/keps/current/nasabare.txt");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {sb.append(inputLine).append(";");}
        in.close();
        inputLine = sb.toString();
        String lines[] = inputLine.split(";");
        for (int i = 0; i< lines.length; i+=3) {
        	Satellite satellite = new Satellite(lines[i],lines[i+1],lines[i+2]);
        	app.satellites.add(satellite);
        }
	}

	public static void main(String[] args) {
		System.out.println("RUNNING MAIN");
	}

}