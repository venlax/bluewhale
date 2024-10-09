package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.service.CommentService;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    CommentService commentService;



    @PostMapping("/commentOrder")
    public ResultVO<Boolean> commentOrder(@RequestBody CommentVO commentVO) {
        return ResultVO.buildSuccess(commentService.commentOrder(commentVO));
    }

    @GetMapping("/getCommentsByCommodityId/{commodityId}")
    public ResultVO<List<CommentVO>> getCommentsByCommodityId(@PathVariable(value = "commodityId") Integer commodityId) {
        return ResultVO.buildSuccess(commentService.getCommentsByCommodityId(commodityId));
    }

    @GetMapping("/getCommentsByUserId/{userId}")
    public ResultVO<List<CommentVO>> getCommentsByUserId(@PathVariable(value = "userId") Integer userId) {
        return ResultVO.buildSuccess(commentService.getCommentsByUserId(userId));
    }

    @GetMapping("/getCommentsByStoreId/{storeId}")
    public ResultVO<List<CommentVO>> getCommentsByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        return ResultVO.buildSuccess(commentService.getCommentsByStoreId(storeId));
    }

    @GetMapping("/getCommentByOrderId/{orderId}")
    public ResultVO<CommentVO> getCommentByOrderId(@PathVariable(value = "orderId") Integer orderId) {
        return ResultVO.buildSuccess(commentService.getCommentByOrderId(orderId));
    }

    @GetMapping("/getScoreByCommodityId/{commodityId}")
    public ResultVO<BigDecimal> getScoreByCommodityId(@PathVariable(value = "commodityId") Integer commodityId) {
        return ResultVO.buildSuccess(commentService.getScoreByCommodityId(commodityId));
    }

    @GetMapping("/getScoreByStoreId/{storeId}")
    public ResultVO<BigDecimal> getScoreByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        return ResultVO.buildSuccess(commentService.getScoreByStoreId(storeId));
    }

    @GetMapping("/getCommentCountByCommodityId/{commodityId}")
    public ResultVO<Integer> getCommentCountByCommodityId(@PathVariable(value = "commodityId") Integer commodityId) {
        return ResultVO.buildSuccess(commentService.getCommentCountByCommodityId(commodityId));
    }

    @GetMapping("/getCommentCountByStoreId/{storeId}")
    public ResultVO<Integer> getCommentCountByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        return ResultVO.buildSuccess(commentService.getCommentCountByStoreId(storeId));
    }
}
