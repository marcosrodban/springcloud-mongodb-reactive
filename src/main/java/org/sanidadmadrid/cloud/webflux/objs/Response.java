package org.sanidadmadrid.cloud.webflux.objs;

public class Response<T> {
	private int code;
	private String status;
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static class ResponseBuilder<T> {

		private int code;
		private String status;
		private T data;

		public ResponseBuilder code(int code) {
			this.code = code;
			return this;

		}

		public ResponseBuilder status(String status) {
			this.status = status;
			return this;

		}

		public ResponseBuilder data(T data) {
			this.data = data;
			return this;

		}

		public Response<T> build() {
			Response<T> theRespose = new Response<T>();
			theRespose.setCode(code);
			theRespose.setStatus(status);
			theRespose.setData(data);
			return theRespose;

		}

	}

}