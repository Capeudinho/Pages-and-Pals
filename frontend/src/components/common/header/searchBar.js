import { Link, useNavigate } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";

import "./searchBar.css";


function SearchBar() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const [showProfileOptions, setShowProfileOptions] = useState(false);
    const { setAlert } = useContext(alertContext);
    const { confirm, setConfirm } = useContext(confirmContext);
    const navigate = useNavigate();

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if (confirm === "logout") {
                        setConfirm(null);
                        handleLogOut();
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [confirm]
        );

    function handleConfirmLogOut() {
        setConfirm([{ identifier: "logout", text: "Log out from account?", options: [{ type: "logout", text: "Log out" }, { type: "cancel", text: "Cancel" }] }]);
    }

    function handleLogOut() {
        localStorage.clear();
        setAlert([{ type: "success", text: "Logged out from account." }]);
        setLoggedAccount(null);
        navigate("/");
    }

    return (
        <div className="searchBarArea">

            <input type="text" placeholder="Search books" />
            <button>Search</button>

            <div className="profileContainer"
                onMouseEnter={() => setShowProfileOptions(true)}
                onMouseLeave={() => setShowProfileOptions(false)}
            >
                <div
                    className="ownerPicture"
                    style={{ backgroundImage: `url(${loggedAccount?.picture})` }}
                />
                {showProfileOptions && (
                    <div className="profileOptions">
                        <Link
                            className="advanced"
                            to="/search/advanced"
                        >
                            Advanced Search
                        </Link>
                        
                        <Link
                            className="account"
                            to={`/account/view/${loggedAccount?.id}`}>
                            Your Profile
                        </Link>

                        <button onClick={handleConfirmLogOut}>Log Out</button>
                    </div>
                )}
            </div>

        </div>
    );
}
export default SearchBar;