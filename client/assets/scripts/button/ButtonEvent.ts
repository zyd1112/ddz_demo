import { _decorator, Button, Camera, Component, director, EditBox, EventHandler, EventTouch, geometry, Label, Node, NodeEventType, PhysicsSystem, UITransform } from 'cc';
import { Gloabal } from '../Global';
import { CardLoader } from '../player/CardLoader';
import { CardManager } from '../player/CardManager';
import { reqEnterRoom, reqGetMailCode, reqLeaveRoom, reqMailLogin, reqNoSend, reqReady, reqScramble, reqSendCard, reqStart, reqSuggest, reqVisitorLogin } from '../api/request';
import { GameManager } from '../framework/GameManager';
import { resourceInit } from '../api/game';
const { ccclass, property } = _decorator;

@ccclass('ButtonEvent')
export class ButtonEvent extends Component {

    @property(Node)
    private cardSelf: Node = null;
    @property(Node)
    private dialog: Node = null;

    public visitorLogin(){
        reqVisitorLogin();
    }

    public enterRoom(){
        console.log(Gloabal.uid)
        if(Gloabal.uid == 0){
            alert("请先登录")
        }else{
            const gameManager = GameManager.getInstance();
            director.loadScene("GameScene", () => {
                resourceInit(gameManager);                
                reqEnterRoom();
            })
        }
    }

    public sendCard(){
        console.log("出牌");
        const children = this.cardSelf.children;
        const remove= [];
        for(let i = 0; i < children.length; i++){
            let cardLoader = children[i].getComponent(CardLoader);
            if(cardLoader.getCard().send){
                remove.push(cardLoader.getCard());
            }
        }
        reqSendCard(remove);
    }

    public noSendCard(){
        console.log("不出");
        this.cardSelf.getComponent(CardManager).reset()
        reqNoSend(Gloabal.uid);
    }

    public suggest(){
        reqSuggest(Gloabal.uid);
    }

    public ready(){
        reqReady()
    }

    public gameStart(){
        reqStart();
    }

    public scramble(event: Event,customEventData: string){
        console.log(customEventData);
        let flag = false;
        if(customEventData == "0"){
            flag = true;
        }
        reqScramble(flag);
    }

    public closeReward(){
        GameManager.getInstance().rewardTable.active = false;
    }

    public leave(){
        reqLeaveRoom();
        director.loadScene("HomeScene")
    }

    public showLoginBg(){
        const canvas = director.getScene().getChildByName("Canvas");
        canvas.getChildByName("button").active = false;
        canvas.getChildByName("loginBg").active = true;
    }

    public getCode(){
        const mail = this.dialog.getChildByName("mail").getComponent(EditBox).string;
        if(mail == ""){
            alert("请输入邮箱")
        }else{
            reqGetMailCode(mail);
        }
    }

    public mailLogin(){
        const mail = this.dialog.getChildByName("mail").getComponent(EditBox).string;
        const code = this.dialog.getChildByName("code").getComponent(EditBox).string;
        if(mail == "" || code == ""){
            alert("请输入信息")
        }else{
            reqMailLogin(mail, code);
        }
    }


}




