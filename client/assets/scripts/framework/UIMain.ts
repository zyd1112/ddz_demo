import { _decorator, Component, Node, SystemEvent } from 'cc';
const { ccclass, property } = _decorator;

@ccclass('UIMain')
export class UIMain extends Component {
    @property(Node)
    private cardSelf: Node = null;
    start() {

    }

    update(deltaTime: number) {
        
    }

    private _touchStart(){
        
    }
}

