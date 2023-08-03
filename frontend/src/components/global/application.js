import React, {useState, useEffect} from "react";
import Routing from "./routing.js";
import api from "../../services/api.js";

import AlertList from "../alert/list.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./application.css";

function Application ()
{
    const [loggedAccount, setLoggedAccount] = useState("loading");
    const [alert, setAlert] = useState([]);
    const [overlay, setOverlay] = useState(false);
    
    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (localStorage.getItem("account") !== null)
                {
                    setOverlay(true);
                    try
                    {
                        var account = JSON.parse(localStorage.getItem("account"));
                        var response = await api.post("/account/login/"+account?.email+"/"+account.password);
                        setLoggedAccount(response?.data);
                    }
                    catch (exception)
                    {
                        localStorage.clear();
                        setLoggedAccount(null);
                    }
                    setOverlay(false);
                }
                else
                {
                    setLoggedAccount(null);
                }
            }
            runEffect();
            return (() => {mounted = false;});
        },
        []
    );
    
    return (
        <div className = "area applicationArea">
            <loggedAccountContext.Provider value = {{loggedAccount, setLoggedAccount}}>
            <alertContext.Provider value = {{alert, setAlert}}>
            <overlayContext.Provider value = {{overlay, setOverlay}}>
                {
                    loggedAccount?.id !== undefined || loggedAccount === null ?
                    <>
                        {
                            overlay ?
                            <div className = "overlay"/> :
                            <></>
                        }
                        <AlertList/>
                        <Routing/>
                    </> :
                    <></>
                }
            </overlayContext.Provider>
            </alertContext.Provider>
            </loggedAccountContext.Provider>
        </div>
    );
}

export default Application;