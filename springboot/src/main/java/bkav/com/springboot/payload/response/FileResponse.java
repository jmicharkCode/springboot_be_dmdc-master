package bkav.com.springboot.payload.response;

import lombok.Data;

@Data
public class FileResponse {
    private String fileName;
    private String fileDownload;
    private String fileType;
    private long size;

    public FileResponse(String fileName, String fileDownload, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownload = fileDownload;
        this.fileType = fileType;
        this.size = size;
    }
}
