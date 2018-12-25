package com.walletech.queue.message;

import com.walletech.po.ModifyBalanceStrategyInfo;

public class ModifyBalanceStrategyACKMessage extends Message {
    private ModifyBalanceStrategyInfo modifyBalanceStrategyInfo;

    public ModifyBalanceStrategyInfo getModifyBalanceStrategyInfo() {
        return modifyBalanceStrategyInfo;
    }

    public void setModifyBalanceStrategyInfo(ModifyBalanceStrategyInfo modifyBalanceStrategyInfo) {
        this.modifyBalanceStrategyInfo = modifyBalanceStrategyInfo;
    }
}
