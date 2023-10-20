package com.nnk.springboot.dto;

import com.nnk.springboot.domain.Trade;
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
public class TradeDto {
    private Integer id;
    @NotBlank(message = "Account is mandatory")
    private String account;
    @NotBlank(message = "Type is mandatory")
    private String type;
    @NotNull(message = "Buy Quantity is mandatory")
    @Min(value = 0, message = "it must be positive")
    private Double buyQuantity;

    public static TradeDto mapFromTrade(Trade trade) {
        return new TradeDto(trade.getId(),trade.getAccount(),trade.getType(),trade.getBuyQuantity());
    }

    public static List<TradeDto> mapFromTrades(List<Trade> trades) {
        return trades.stream().map(TradeDto::mapFromTrade).toList();
    }

}

