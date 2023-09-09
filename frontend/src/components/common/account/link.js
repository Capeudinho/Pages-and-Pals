import React from "react";
import {Link} from "react-router-dom";

import "./link.css";

function AccountLink({account})
{
    return (
        <div className = "accountLinkArea">
            <Link
            className = "accountPicture"
            to = {"/account/view/"+account?.id}
            style = {{backgroundImage: "url("+account?.picture+")"}}
            />
            <Link
            className = "accountName"
            to = {"/account/view/"+account?.id}
            >
                {account?.name}
            </Link>
        </div>
    );
}

export default AccountLink;