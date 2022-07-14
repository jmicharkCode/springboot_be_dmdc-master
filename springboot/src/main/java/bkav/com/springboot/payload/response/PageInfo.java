package bkav.com.springboot.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageInfo {

    private int page_idx;

    private int page_size;

    private int total_record;

    private int total_page;

    private int begin_idx;

    private int end_idx;

    public PageInfo() {
    }

    public PageInfo(int page_idx, int page_size, int total_record, int total_page, int begin_idx, int end_idx) {
        this.page_idx = page_idx;
        this.page_size = page_size;
        this.total_record = total_record;
        this.total_page = total_page;
        this.begin_idx = begin_idx;
        this.end_idx = end_idx;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "page_idx=" + page_idx +
                ", page_size=" + page_size +
                ", total_record=" + total_record +
                ", total_page=" + total_page +
                ", begin_idx=" + begin_idx +
                ", end_idx=" + end_idx +
                '}';
    }
}
