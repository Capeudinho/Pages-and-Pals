import React, {useState, useContext} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./enter.css";

function AccountEnter()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [signUpName, setSignUpName] = useState("");
    const [signUpEmail, setSignUpEmail] = useState("");
    const [signUpPassword, setSignUpPassword] = useState("");
    const [showSignUpPassword, setShowSignUpPassword] = useState(false);
    const [logInEmail, setLogInEmail] = useState("");
    const [logInPassword, setLogInPassword] = useState("");
    const [showLogInPassword, setShowLogInPassword] = useState(false);
    const [mode, setMode] = useState("signup");
    const navigate = useNavigate();

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

    function handleChangeModeSignUp()
    {
        setMode("signup");
    }

    function handleChangeModeLogIn()
    {
        setMode("login");
    }

    async function handleSignUp()
    {
        setOverlay(true);
        try
        {
            const response = await api.post
            (
                "/account/signup",
                {
                    name: signUpName,
                    email: signUpEmail,
                    password: signUpPassword
                }
            );
            localStorage.setItem("account", JSON.stringify(response.data));
            setLoggedAccount(response.data);
            setAlert([{text: "Account created.", type: "success", key: Math.random()}]);
            navigate("/");
        }
        catch (exception)
        {
            if (exception.response.hasOwnProperty("data"))
            {
                if (exception.response.data === "email taken")
                {
                    setAlert([{text: "E-mail is taken.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid name")
                {
                    setAlert([{text: "Name is invalid.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid email")
                {
                    setAlert([{text: "E-mail is invalid.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid password")
                {
                    setAlert([{text: "Password is invalid.", type: "warning", key: Math.random()}]);
                }
            }
        }
        setOverlay(false);
    }

    async function handleLogIn()
    {
        setOverlay(true);
        try
        {
            const response = await api.post("/account/login/"+logInEmail+"/"+logInPassword);
            localStorage.setItem("account", JSON.stringify(response.data));
            setLoggedAccount(response.data);
            setAlert([{text: "Logged into account.", type: "success", key: Math.random()}]);
            navigate("/");
        }
        catch (exception)
        {
            if (exception.response.hasOwnProperty("data"))
            {
                if (exception.response.data === "incorrect information")
                {
                    setAlert([{text: "E-mail, or password is incorrect.", type: "warning", key: Math.random()}]);
                }
            }
        }
        setOverlay(false);
    }

    return (
        <div className = "area accountEnterArea">
            <div className = "mainBox">
                <div className = "topBox">
                    <div className = "text topText">
                        Welcome to
                    </div>
                    <div className = "text bottomText">
                        Pages & Pals
                    </div>
                </div>
                <div className = "modeBox">
                    <button
                    className = "modeButton modeSignUpButton"
                    onClick = {() => {handleChangeModeSignUp()}}
                    style = {{backgroundColor: mode === "signup" ? "#ffffff" : "#cccccc"}}
                    >
                        Sign up
                    </button>
                    <button
                    className = "modeButton modeLogInButton"
                    onClick = {() => {handleChangeModeLogIn()}}
                    style = {{backgroundColor: mode === "login" ? "#ffffff" : "#cccccc"}}
                    >
                        Log in
                    </button>
                </div>
                <div className = "formBox">
                    <div
                    className = "innerFormBox"
                    style = {{display: mode === "signup" ? "block" : "none"}}
                    >
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
                        <button
                        className = "completeButton"
                        onClick = {() => {handleSignUp()}}
                        >
                            Sign up
                        </button>
                    </div>
                    <div
                    className = "innerFormBox"
                    style = {{display: mode === "login" ? "block" : "none"}}
                    >
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
                        <button
                        className = "completeButton"
                        onClick = {() => {handleLogIn()}}
                        >
                            Log in
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AccountEnter;