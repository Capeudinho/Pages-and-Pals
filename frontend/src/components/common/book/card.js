import React from "react";
import {Link} from "react-router-dom";

import BookshelfManage from "../bookshelf/manage";

import "./card.css";

function BookCard({book, bookIndex, remove, removeable, manageable})
{
    return (
        <div className = "bookCardArea">
            <Link to = {"/book/view/"+book?.apiId}>
                <div className = {"book"+(removeable? " spacedRemove" : "")+(manageable? " spacedManage" : "")}>
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
                            <div className = "score">{book?.score} ★</div> :
                            <></>
                        }
                    </div>
                </div>
            </Link>
            {
                removeable ?
                <button
                className = "removeButton"
                onClick = {() => {remove(bookIndex)}}
                >
                    X
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