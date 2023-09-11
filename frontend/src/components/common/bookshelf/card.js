import React from "react";
import {useNavigate} from "react-router-dom";

import BookshelfCovers from "./covers";
import AccountLink from "../account/link";

import "./card.css";

function BookshelfCard({bookshelf, linkable})
{
    const navigate = useNavigate();

    function handleClick(event)
    {
        if (event?.target?.closest(".accountLinkArea") === null)
        {
            navigate("/bookshelf/from/"+bookshelf?.owner?.id+"/view/"+bookshelf?.id);
        }
    }

    return (
        <div
        className = "bookshelfCardArea"
        onClick = {(event) => {handleClick(event)}}
        >
            <BookshelfCovers covers = {[bookshelf?.covers?.[0], bookshelf?.covers?.[1], bookshelf?.covers?.[2]]}/>
            <div className = "info">
                <div className = "bookshelfName">
                    {bookshelf?.name}
                </div>
                {
                    linkable ?
                    <AccountLink account={bookshelf?.owner}/> :
                    <></>
                }
                <div className = "description">
                    {bookshelf?.description}
                </div>
            </div>
        </div>
    );
}

export default BookshelfCard;