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
    const [loggedAccount, setLoggedAccount] = useState(null);
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
                        const account = JSON.parse(localStorage.getItem("account"));
                        const response = await api.get("/account/findbyid/"+account.id);
                        setLoggedAccount(response.data);
                    }
                    catch (exception)
                    {
                        if (exception.response.data === "incorrect id")
                        {
                            localStorage.clear();
                        }
                    }
                    setOverlay(false);
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
                <div
                className = "overlay"
                style = {{display: overlay ? "block" : "none"}}
                />
                <AlertList/>
                <Routing/>
            </overlayContext.Provider>
            </alertContext.Provider>
            </loggedAccountContext.Provider>
        </div>
    );
}

export default Application;