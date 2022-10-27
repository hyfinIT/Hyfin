package hyphin.dto;

import lombok.Data;

@Data
public class ClickItemDto {

    private String text;
    private String imageData;
    private String style;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isSessionExpired;
}
