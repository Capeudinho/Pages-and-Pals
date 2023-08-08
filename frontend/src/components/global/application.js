import React, {useState, useEffect} from "react";
import {BrowserRouter} from "react-router-dom";
import Routing from "./routing.js";
import api from "../../services/api.js";

import AlertList from "../common/alert/list.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import confirmContext from "../context/confirm.js";
import overlayContext from "../context/overlay.js";
import clickContext from "../context/click.js";
import scrollContext from "../context/scroll.js";

import "./application.css";

function Application ()
{
    const [loggedAccount, setLoggedAccount] = useState("loading");
    const [alert, setAlert] = useState(null);
    const [confirm, setConfirm] = useState(null);
    const [overlay, setOverlay] = useState(false);
    const [click, setClick] = useState(null);
    const [scroll, setScroll] = useState(null);
    
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

    function handleChangeClick(event)
    {
        setClick(event);
    }
    
    return (
        <div
        className = "applicationArea"
        onClick = {(event) => {handleChangeClick(event)}}
        >
            <BrowserRouter>
                <loggedAccountContext.Provider value = {{loggedAccount, setLoggedAccount}}>
                <alertContext.Provider value = {{alert, setAlert}}>
                <confirmContext.Provider value = {{confirm, setConfirm}}>
                <overlayContext.Provider value = {{overlay, setOverlay}}>
                <clickContext.Provider value = {{click, setClick}}>
                <scrollContext.Provider value = {{scroll, setScroll}}>
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
                </scrollContext.Provider>
                </clickContext.Provider>
                </overlayContext.Provider>
                </confirmContext.Provider>
                </alertContext.Provider>
                </loggedAccountContext.Provider>
            </BrowserRouter>
        </div>
    );
}

export default Application;