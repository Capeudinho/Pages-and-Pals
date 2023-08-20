import React, {useState, useContext} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";

import "./enter.css";

function AccountEnter()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [logInEmail, setLogInEmail] = useState("");
    const [logInPassword, setLogInPassword] = useState("");
    const [showLogInPassword, setShowLogInPassword] = useState(false);
    const [signUpName, setSignUpName] = useState("");
    const [signUpEmail, setSignUpEmail] = useState("");
    const [signUpPassword, setSignUpPassword] = useState("");
    const [showSignUpPassword, setShowSignUpPassword] = useState(false);
    const [mode, setMode] = useState("login");
    const navigate = useNavigate();

    function handleChangeLogInEmail(event)
    {
        setLogInEmail(event.target.value);
    }

    function handleChangeLogInPassword(event)
    {
        setLogInPassword(event.target.value);
    }

    function handleChangeShowLogInPassword()
    {
        setShowLogInPassword(!showLogInPassword);
    }

    function handleChangeSignUpName(event)
    {
        setSignUpName(event.target.value);
    }

    function handleChangeSignUpEmail(event)
    {
        setSignUpEmail(event.target.value);
    }

    function handleChangeSignUpPassword(event)
    {
        setSignUpPassword(event.target.value);
    }

    function handleChangeShowSignUpPassword()
    {
        setShowSignUpPassword(!showSignUpPassword);
    }

    function handleChangeMode(newMode)
    {
        setMode(newMode);
    }

    async function handleLogIn()
    {
        try
        {
            if (logInEmail === "" || logInPassword === "")
            {
                setAlert([{type: "warning", text: "E-mail, or password is incorrect."}]);
                return;
            }
            setOverlay(true);
            const response = await api.get("/account/login/"+logInEmail+"/"+logInPassword);
            setOverlay(false);
            localStorage.setItem("account", JSON.stringify(response?.data));
            setLoggedAccount(response?.data);
            setAlert([{type: "success", text: "Logged into account"}]);
            navigate("/");
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "incorrect information")
            {
                setAlert([{type: "warning", text: "E-mail, or password is incorrect."}]);
            }
        }
    }

    async function handleSignUp()
    {
        try
        {
            setOverlay(true);
            const response = await api.post
            (
                "/account/signup",
                {
                    name: signUpName,
                    email: signUpEmail,
                    password: signUpPassword
                }
            );
            setOverlay(false);
            localStorage.setItem("account", JSON.stringify(response?.data));
            setLoggedAccount(response?.data);
            setAlert([{type: "success", text: "Account created."}]);
            navigate("/");
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "email taken")
            {
                setAlert([{type: "warning", text: "E-mail is taken."}]);
            }
            else if (exception?.response?.data === "invalid name")
            {
                setAlert([{type: "warning", text: "Name is invalid."}]);
            }
            else if (exception?.response?.data === "invalid email")
            {
                setAlert([{type: "warning", text: "E-mail is invalid."}]);
            }
            else if (exception?.response?.data === "invalid password")
            {
                setAlert([{type: "warning", text: "Password is invalid."}]);
            }
        }
    }

    return (
        <div className = "page accountEnterArea">
            <div className = "topBox">
                <div className = "text">Welcome to</div>
                <div className = "text">Pages & Pals</div>
            </div>
            <ButtonMode
            modes = {[{text: "Log in", type: "login"}, {text: "Sign up", type: "signup"}]}
            currentMode = {mode}
            setMode = {handleChangeMode}
            />
            <div className = "formBox">
                {
                    mode === "login" ?
                    <>
                        <div className = "label">E-mail</div>
                        <input
                        className = "normalInput"
                        value = {logInEmail}
                        onChange = {(event) => {handleChangeLogInEmail(event)}}
                        spellCheck = {false}
                        />
                        <div className = "label">Password</div>
                        <div className = "passwordBox">
                            <input
                            className = "passwordInput"
                            value = {logInPassword}
                            onChange = {(event) => {handleChangeLogInPassword(event)}}
                            type = {showLogInPassword ? "text" : "password"}
                            spellCheck = {false}
                            />
                            <button
                            className = "showPasswordButton"
                            onClick = {() => {handleChangeShowLogInPassword()}}
                            >
                                {showLogInPassword ? "H" : "S"}
                            </button>
                        </div>
                        <ButtonGroup options = {[{text: "Log in", operation: handleLogIn}]}/>
                    </> :
                    <>
                        <div className = "label">Name</div>
                        <input
                        className = "normalInput"
                        value = {signUpName}
                        onChange = {(event) => {handleChangeSignUpName(event)}}
                        spellCheck = {false}
                        />
                        <div className = "label">E-mail</div>
                        <input
                        className = "normalInput"
                        value = {signUpEmail}
                        onChange = {(event) => {handleChangeSignUpEmail(event)}}
                        spellCheck = {false}
                        />
                        <div className = "label">Password</div>
                        <div className = "passwordBox">
                            <input
                            className = "passwordInput"
                            value = {signUpPassword}
                            onChange = {(event) => {handleChangeSignUpPassword(event)}}
                            type = {showSignUpPassword ? "text" : "password"}
                            spellCheck = {false}
                            />
                            <button
                            className = "showPasswordButton"
                            onClick = {() => {handleChangeShowSignUpPassword()}}
                            >
                                {showSignUpPassword ? "H" : "S"}
                            </button>
                        </div>
                        <ButtonGroup options = {[{text: "Sign up", operation: handleSignUp}]}/>
                    </>
                }
            </div>
        </div>
    );
}

export default AccountEnter;