package com.neurotech.currencyAPI.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair <S,T> {
    private S first;
    private T second;
}
