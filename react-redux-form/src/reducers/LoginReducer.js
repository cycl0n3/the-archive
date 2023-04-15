export default function LoginReducer(
    state = {
        // selectBtn: '',
    },
    action
) {
    // const selectBtnVal = action.selectBtnVal
    // const selectBtn = action.selectBtn
    const data = action.data;
    switch (action.type) {
        case "SUBMIT_FORM_DATA":
            return {
                ...state,
                // selectBtn : selectBtnVal,
            };

        case "SUBMIT_FORM_DATA_CLEAR":
            return {
                ...state,
                // selectBtn : selectBtnVal,
            };
        case "IS_LOGIN":
            return {
                ...state,
                isLogin: data,
            };
        case "IS_REGISTER":
            return {
                ...state,
                isRegister: data,
            };

        default:
            return state;
    }
}
