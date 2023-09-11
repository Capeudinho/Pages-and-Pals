import React from "react";
import {Link} from "react-router-dom";

import "./link.css";

function AccountLink({account})
{
    return (
        <Link
        className = "accountLinkArea"
        to = {"/account/view/"+account?.id}
        >
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
    );
}

export default AccountLink;