import { _decorator, Component, Node } from 'cc';
import { userInit } from '../api/home';
const { ccclass, property } = _decorator;

@ccclass('UserManager')
export class UserManager extends Component {
    
    start() {
        userInit();
    }

}


