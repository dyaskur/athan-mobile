/**
 * 
 */
package com.athan.mobile.download.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import com.athan.mobile.constants.AthanConstants;

/**
 * File download servlet
 * 
 * @author Saad BENBOUZID
 */
public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final int BYTES_DOWNLOAD = 1024;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String fileType = (String) request
				.getParameter(AthanConstants.DOWNLOAD_FILETYPE_PARAM);

		String file = (String) request
				.getParameter(AthanConstants.DOWNLOAD_FILE_PARAM);

		OutputStream os = response.getOutputStream();

		if (AthanConstants.DOWNLOAD_JAD.equals(fileType)) {
			response.setContentType(AthanConstants.MIME_TYPE_JAD);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ FilenameUtils.getName(AthanConstants.CURRENT_ATHAN_JAD));

			InputStream is = getClass().getResourceAsStream(
					AthanConstants.CURRENT_ATHAN_JAD);

			int read = 0;
			byte[] bytes = new byte[BYTES_DOWNLOAD];

			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
		} else if (AthanConstants.DOWNLOAD_JAR.equals(fileType)) {
			response.setContentType(AthanConstants.MIME_TYPE_JAR);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ FilenameUtils.getName(AthanConstants.CURRENT_ATHAN_JAR));

			InputStream is = getClass().getResourceAsStream(
					AthanConstants.CURRENT_ATHAN_JAR);

			int read = 0;
			byte[] bytes = new byte[BYTES_DOWNLOAD];

			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
		} else if (AthanConstants.DOWNLOAD_MP3.equals(fileType)) {
			response.setContentType(AthanConstants.MIME_TYPE_MP3);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ FilenameUtils.getName(AthanConstants.MP3_PATH + file));

			InputStream is = getClass().getResourceAsStream(
					AthanConstants.MP3_PATH + file);

			int read = 0;
			byte[] bytes = new byte[BYTES_DOWNLOAD];

			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
		} else {
			// Other file
		}

		os.flush();
		os.close();
	}
}
