package fileuploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.util.Streams;

public class CustomServletFileUpload extends ServletFileUpload {

	public CustomServletFileUpload(DiskFileItemFactory factory) {
		super(factory);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.fileupload.servlet.ServletFileUpload#parseRequest(javax.servlet.http.HttpServletRequest)
	 */
	public List parseRequest(HttpServletRequest request) throws FileUploadException {
		return parseRequest(new ServletRequestContext(request));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.fileupload.FileUploadBase#parseRequest(org.apache.commons.fileupload.RequestContext)
	 */
	public List<FileItem> parseRequest(RequestContext ctx)
			throws FileUploadException {
		try {
			FileItemIterator iter = getItemIterator(ctx);
			List<FileItem> items = new ArrayList<FileItem>();
			FileItemFactory fac = getFileItemFactory();
			if (fac == null) {
				throw new NullPointerException(
						"No FileItemFactory has been set.");
			}
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				System.out.println("[SEB-DEBUG] item.getName(): "+item.getName());	
				System.out.println("[SEB-DEBUG] item.getName().endsWith(\"/.\"): "+item.getName().endsWith("/."));
				System.out.println("[SEB-DEBUG] item.getName().endsWith(\".\"): "+item.getName().endsWith("."));
				if(item.getName().endsWith("/.") || item.getName().endsWith(".")) {
					// ignore
					while(iter.hasNext()) {
						item = iter.next();
						System.out.println("[SEB-DEBUG] item.getName(): "+item.getName());
						// ignore
						if(!item.getName().endsWith("/.")) break;
					}				
				}
				System.out.println("[SEB-DEBUG] before createItem");
				FileItem fileItem = fac.createItem(item.getFieldName(),
						item.getContentType(), item.isFormField(),
						item.getName());
				System.out.println("[SEB-DEBUG] after createItem");
				try {
					Streams.copy(item.openStream(), fileItem.getOutputStream(),
							true);
				} catch (FileUploadIOException e) {
					throw (FileUploadException) e.getCause();
				} catch (IOException e) {
					throw new IOFileUploadException("Processing of "
							+ MULTIPART_FORM_DATA + " request failed. "
							+ e.getMessage(), e);
				}
				items.add(fileItem);
			}
			return items;
		} catch (FileUploadIOException e) {
			throw (FileUploadException) e.getCause();
		} catch (IOException e) {
			throw new FileUploadException(e.getMessage(), e);
		}
	}
}
