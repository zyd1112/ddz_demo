import { _decorator, Component, instantiate, Node, NodePool, Prefab } from 'cc';
const { ccclass, property } = _decorator;

interface IDictPoll {
    [name: string]: NodePool;
}

interface IDictPrefab {
    [name: string]: Prefab;
}

@ccclass('PoolManager')
export class PoolManager {
    private static INSTANCE: PoolManager = new PoolManager();
    private _dictPoll: IDictPoll = {};
    private _dictPrefab: IDictPrefab = {};

    public getNode(prefab: Prefab, parent: Node){
        let name = prefab.data.name;
        let node: Node = null;
        this._dictPrefab[name] = prefab;
        let pool = this._dictPoll[name];
        if(!pool){
            pool = new NodePool();
        }
        if(pool.size() > 0){
            node = pool.get();
        }else {
            node = instantiate(prefab);
        }
        node.parent = parent;
        node.active = true;
        return node;
    }

    public putNode(node: Node){
        let name = node.name;
        if(!this._dictPoll[name]){
            this._dictPoll[name] = new NodePool();
        }

        this._dictPoll[name].put(node);
    }

    public static getInstance(){
        return this.INSTANCE;
    }
}


