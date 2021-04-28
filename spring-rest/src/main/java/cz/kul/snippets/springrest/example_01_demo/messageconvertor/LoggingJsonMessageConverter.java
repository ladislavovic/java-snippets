package cz.kul.snippets.springrest.example_01_demo.messageconvertor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class LoggingJsonMessageConverter extends MappingJackson2HttpMessageConverter {

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		// TODO test if it goes from API
		KeepDataHttpInputMessage wrapper = new KeepDataHttpInputMessage(inputMessage);
		try {
			return super.read(type, contextClass, wrapper);
		} finally {
			wrapper.log();
		}
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		KeepDataHttpInputMessage wrapper = new KeepDataHttpInputMessage(inputMessage);
		try {
			return super.readInternal(clazz, new KeepDataHttpInputMessage(inputMessage));
		} finally {
			wrapper.log();
		}
	}

	public static class KeepDataHttpInputMessage implements HttpInputMessage {

		private static final int MAX_LENGTH = 4096;

		private HttpInputMessage wrapped;
		private KeepDataInputStream keepDataInputStream;

		public KeepDataHttpInputMessage(HttpInputMessage wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public InputStream getBody() throws IOException {
			if (keepDataInputStream == null) {
				keepDataInputStream = new KeepDataInputStream(wrapped.getBody(), MAX_LENGTH);
			}
			return keepDataInputStream;
		}

		@Override
		public HttpHeaders getHeaders() {
			return wrapped.getHeaders();
		}

		public void log() {
			byte[] data = keepDataInputStream.getData();
			String dataStr = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(data, 0, data.length)).toString();
			if (keepDataInputStream.isKeptAll()) {
				dataStr = dataStr + "... continue";
			}
			System.out.println(dataStr);
		}

	}

	public static class KeepDataInputStream extends FilterInputStream {

		private static final int DEFAULT_MAX_LENGTH = 4096;

		private byte[] data;
		private int currentLength = 0;
		private int maxLength;
		private boolean keptAll = true;

		public KeepDataInputStream(InputStream inner, Integer maxLength) {
			super(inner);
			this.maxLength = maxLength == null ? DEFAULT_MAX_LENGTH : maxLength;
			data = new byte[maxLength];
		}

		public byte[] getData() {
			return data;
		}

		public boolean isKeptAll() {
			return keptAll;
		}

		@Override
		public int read() throws IOException {
			int res = super.read();
			if (res >= 0) {
				addByte((byte) res);
			}
			return res;
		}

		@Override
		public int read(byte[] b) throws IOException {
			int res = super.read(b);
			if (res >= 0) {
				addBytes(b, res);
			}
			return res;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int res = super.read(b, off, len);
			if (res >= 0) {
				addBytes(b, res);
			}
			return res;
		}

		private void addByte(byte b) {
			if (currentLength >= maxLength) {
				keptAll = false;
				return;
			}
			data[currentLength++] = b;
		}

		private void addBytes(byte[] bytea, int length) {
			if (currentLength >= maxLength) {
				keptAll = false;
				return;
			}
			int availableLength = maxLength - currentLength;
			int actualLength;
			if (availableLength < length) {
				actualLength = availableLength;
				keptAll = false;
			} else {
				actualLength = length;
			}
			System.arraycopy(bytea, 0, data, currentLength, actualLength);
			currentLength += actualLength;
		}

	}


}
