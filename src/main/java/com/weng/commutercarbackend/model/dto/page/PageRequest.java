package com.weng.commutercarbackend.model.dto.page;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest
{
    @NotNull(message = "当前页不能为空")
    @Min(value = 1,message = "当前页不能小于1")
    private Integer current=1;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1,message = "每页条数不能小于1")
    private Integer size=10;

}
