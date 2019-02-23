package com.example.kinso.testmarket;

/**
 * Created by kinso on 2019-01-31.
 */

public class CardMessageInfo {
    public String WriteNum, Title, Receiver, Sender, Message, SendDay, MyUserCode;
    public CardMessageInfo(
                                String WriteNum,
                                String Title,
                                String Receiver,
                                String Sender,
                                String Message,
                                String SendDay,
                                String MyUserCode){
        this.WriteNum = WriteNum;
        this.Title = Title;
        this.Receiver = Receiver;
        this.Sender = Sender;
        this.Message = Message;
        this.SendDay = SendDay;
        this.MyUserCode = MyUserCode;
    }

}
