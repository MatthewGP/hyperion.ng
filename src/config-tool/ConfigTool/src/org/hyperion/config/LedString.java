package org.hyperion.config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import org.hyperion.config.spec.ColorConfig;
import org.hyperion.config.spec.DeviceConfig;
import org.hyperion.config.spec.ImageProcessConfig;
import org.hyperion.config.spec.Led;
import org.hyperion.config.spec.LedFrameConstruction;
import org.hyperion.config.spec.MiscConfig;

public class LedString {
	
	/** The configuration of the output device */
	public DeviceConfig mDeviceConfig = new DeviceConfig();

	/** THe configuration of the 'physical' led frame */
	public LedFrameConstruction mLedFrameConfig = new LedFrameConstruction();
	/** The configuration of the image processing */
	public ImageProcessConfig mProcessConfig = new ImageProcessConfig();
	
	/** The color adjustment configuration */
	public ColorConfig mColorConfig = new ColorConfig();
	
	/** The miscellaneous configuration (bootsequence, blackborder detector, etc) */
	public MiscConfig mMiscConfig = new MiscConfig();
	
	/** The translation of the led frame construction and image processing to individual led configuration */
	public Vector<Led> leds;
	
	public void saveConfigFile(String mFilename) throws IOException {
		
		try (FileWriter fw = new FileWriter(mFilename)) {
			fw.write("// Automatically generated configuration file for 'Hyperion'\n");
			fw.write("// Generated by: 'Hyperion configuration Tool\n");
			fw.write("\n");
			fw.write("{\n");
			
			String deviceJson = mDeviceConfig.toJsonString();
			fw.write(deviceJson + ",\n");
			
			String colorJson = mColorConfig.toJsonString();
			fw.write(colorJson + ",\n");

			String ledJson = ledToJsonString();
			fw.write(ledJson + ",\n");
			
			String miscJson = mMiscConfig.toJsonString();
			fw.write(miscJson + "\n");
			
			fw.write("}\n");
		} catch (IOException e) {
			throw e;
		}
	}
	
	String ledToJsonString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\t\"leds\" : \n");
		strBuf.append("\t[\n");
		
		for (Led led : leds)
		{
			strBuf.append("\t\t{\n");
			strBuf.append(String.format(Locale.ROOT, "\t\t\t\"index\" : %d,\n", led.mLedSeqNr));
			strBuf.append(String.format(Locale.ROOT, "\t\t\t\"hscan\" : { \"minimum\" : %.4f, \"maximum\" : %.4f },\n", led.mImageRectangle.getMinX(), led.mImageRectangle.getMaxX()));
			strBuf.append(String.format(Locale.ROOT, "\t\t\t\"vscan\" : { \"minimum\" : %.4f, \"maximum\" : %.4f }\n", led.mImageRectangle.getMinY(), led.mImageRectangle.getMaxY()));
			if (led != leds.lastElement()) {
				strBuf.append("\t\t},\n");
			} else {
				strBuf.append("\t\t}\n");
			}
		}
		
		strBuf.append("\t]");
		
		return strBuf.toString();
	}
}