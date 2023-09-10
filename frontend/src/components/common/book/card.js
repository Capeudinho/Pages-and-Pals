import React from "react";
import {useNavigate} from "react-router-dom";

import BookshelfManage from "../bookshelf/manage";

import "./card.css";

function BookCard({book, bookIndex, remove, removeable, manageable})
{
    const navigate = useNavigate();

    function handleClick(event)
    {
        if (event?.target?.closest(".bookshelfManageArea") === null && event?.target?.closest(".removeButton") === null)
        {
            navigate("/book/view/"+book?.apiId);
        }
    }

    return (
        <div
        className = "bookCardArea"
        onClick = {(event) => {handleClick(event)}}
        >
            <div
            className = "cover"
            style = {{backgroundImage: "url("+book?.cover+")"}}
            />
            <div className = "bookInfoBox">
                {
                    book?.title !== "" ?
                    <div className = "title">{book?.title}</div> :
                    <></>
                }
                {
                    book?.authors?.length > 0 ?
                    <div className = "authors">
                        {
                            book?.authors?.map
                            (
                                (author, authorIndex) =>
                                {
                                    return (
                                        <div
                                        className = "author"
                                        key = {authorIndex}
                                        >
                                            {author}
                                        </div>
                                    );
                                }
                            )
                        }
                    </div> :
                    <></>
                }
                {
                    book?.categories?.length > 0 ?
                    <div className = "categories">
                        {
                            book?.categories?.map
                            (
                                (category, categoryIndex) =>
                                {
                                    return (
                                        <div
                                        className = "category"
                                        key = {categoryIndex}
                                        >
                                            {category}
                                        </div>
                                    );
                                }
                            )
                        }
                    </div> :
                    <></>
                }
                {
                    book?.score !== null ?
                    <div className = "score">{book?.score} <b>★</b></div> :
                    <></>
                }
            </div>
            {
                removeable ?
                <button
                className = "removeButton"
                onClick = {() => {remove(bookIndex)}}
                >
                    ✘
                </button> :
                <></>
            }
            {
                manageable ?
                <BookshelfManage book = {book}/> :
                <></>
            }
        </div>
    );
}

export default BookCard;