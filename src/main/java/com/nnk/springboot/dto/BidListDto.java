package com.nnk.springboot.dto;

import com.nnk.springboot.domain.BidList;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidListDto {
    private Integer id;
    @NotBlank(message = "Account is mandatory")
    private String account;
    @NotBlank(message = "Type is mandatory")
    private String type;
    @NotNull(message = "Bid Quantity is mandatory")
    @Min(value = 0, message = "it must be positive")
    private Double bidQuantity;

    public static BidListDto mapFromBidList(BidList bidList) {
        return new BidListDto(bidList.getId(),bidList.getAccount(),bidList.getType(),bidList.getBidQuantity());
    }

    public static List<BidListDto> mapFromBidLists(List<BidList> bidLists) {
        return bidLists.stream().map(BidListDto::mapFromBidList).toList();
    }

}

