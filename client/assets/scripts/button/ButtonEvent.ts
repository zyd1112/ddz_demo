import { _decorator, Button, Component, Node, NodeEventType, UITransform } from 'cc';
import { CardManager } from '../card/CardManager';
import { CardLoader } from '../card/CardLoader';
import { PoolManager } from '../framework/PoolManager';
const { ccclass, property } = _decorator;

@ccclass('ButtonEvent')
export class ButtonEvent extends Component {

    @property(Node)
    private cardSelf: Node = null;
    @property(Node)
    private gabage: Node = null;

    start() {
        this._addButton(this.node, () => {
            const children = this.cardSelf.children;
            const cardManager = this.cardSelf.getComponent(CardManager);
            const remove= [];
            for(let i = 0; i < children.length; i++){
                if(children[i].getComponent(CardLoader).isSend()){
                    remove.push(children[i]);
                }
            }
            if(remove.length > 0){
                this.gabage.removeAllChildren();
                let offset = 40;
                let lx = -(Math.floor(remove.length / 2) * offset);
                for(let i = 0; i < remove.length; i++){
                    PoolManager.getInstance().putNode(remove[i]);
                    const gabage = PoolManager.getInstance().getNode(cardManager.cardPrefab, this.gabage);
                    gabage.setPosition(lx, 0, 0);
                    lx += offset;
                }
                
                cardManager.refresh();
            }
        });
    }

    private _addButton(node: Node, callback: Function) {
        const button = node.addComponent(Button);
        button.node.on(NodeEventType.TOUCH_END, callback);
    }
    
}


