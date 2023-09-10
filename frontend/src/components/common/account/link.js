import React from "react";
import {Link} from "react-router-dom";

import "./link.css";

function AccountLink({account})
{
    return (
        <div className = "accountLinkArea">
            <Link to = {"/account/view/"+account?.id}>
                <div
                className = "accountPicture"
                style = {{backgroundImage: "url("+account?.picture+")"}}
                />
                <div
                className = "accountName"
                >
                    {account?.name}
                </div>
            </Link>
        </div>
    );
}

export default AccountLink;