import { Label, Prefab, director, resources } from "cc";
import { Path } from "../constant/path";
import { GameManager } from "../framework/GameManager";
import { Gloabal } from "../Global";
import { PoolManager } from "../framework/PoolManager";

export function resourceLoad(){
    resources.load(Path.LANDOWNER, Prefab, (err, prefab) => {
        GameManager.getInstance().landowner = prefab;
    })
    resources.load(Path.ROOMHOST, Prefab, (err, prefab) => {
        GameManager.getInstance().roomHostImage = prefab;
    })
    GameManager.getInstance().headImages = [];
    resources.load(Path.HEAD_IMAGE_1, Prefab, (err, prefab) => {
        GameManager.getInstance().headImages.push(prefab);
        resources.load(Path.HEAD_IMAGE_2, Prefab, (err, prefab) => {
            GameManager.getInstance().headImages.push(prefab);
            resources.load(Path.HEAD_IMAGE_3, Prefab, (err, prefab) => {
                GameManager.getInstance().headImages.push(prefab);
            })
        })
    })

}

export function userInit(){
    const canvas = director.getScene().getChildByName("Canvas");
    for(let i = 0; i < canvas.children.length; i++){
        const node = canvas.children[i];
        switch(node.name){
            case "vistorLogin":
                node.active = Gloabal.uid == 0;
                break;
            case "user":
                if(Gloabal.uid == 0){
                    node.active = false;
                }else{
                    const name = node.getChildByName("name");
                    name.getComponent(Label).string = Gloabal.username;
                    const image = PoolManager.getInstance().getNode(Gloabal.image, node);
                    image.setPosition(name.position.x + 130, name.position.y, 0);
                    const joyBeans = node.getChildByName("joyBeans");
                    let value = Gloabal.joyBeans;
                    let unit = ""
                    if(value >= 10000){
                        value /= 10000;
                        unit = "万";
                    }else if(value >= 100000000){
                        value /= 100000000;
                        unit = "亿";
                    }
                    joyBeans.getChildByName("value").getComponent(Label).string = value + unit;
                    node.active = true; 
                }
                break;
        }
    }
}