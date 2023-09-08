import React from "react";
import { Link } from "react-router-dom";
import "./card.css";

function ReviewCard({review, page}) {

    function handleFormatDate(date) {
        if (date !== null && date !== undefined) {
            var dateArray = date?.split("-");
            var newDate = dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
            return newDate;
        }
        else {
            return undefined;
        }
    }
// checar se está na página do usuário ou do livro
    return (
        <div className="reviewCardArea">
            <div
            className="cover"
            style={{ backgroundImage: "url(" + review?.cover + ")" }}
            />
            <div className="info">
                <div className="title">
                    {review?.title}
                </div>
                <div className="middleBox">
                    <Link
                    className = "ownerLink"
                    to = {"/account/view/"+review?.owner?.id}
                    >
                        <div
                        className = "ownerPicture"
                        style = {{backgroundImage: "url("+review?.owner?.picture+")"}}
                        />
                        <div className = "ownerName">{review?.owner?.name}</div>
                    </Link>
                    <div className="creationDate">
                        Created in {handleFormatDate(review?.creationDate)}
                    </div>
                    <div className="editionDate">
                        Edited in {handleFormatDate(review?.editionDate)}
                    </div>
                    <div className="bookScore">
                        {review?.bookScore}
                    </div>
                </div>
                <div className="text">
                    {review?.text}
                </div>
            </div>
        </div>
    );
}
export default ReviewCard;