/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.javademo.mybatis.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.javademo.mybatis.Vo.QuerySkinVo;
import com.example.javademo.mybatis.Vo.GetListItemVo;
import com.example.javademo.mybatis.entity.Skin;

/**
 * 指定这是一个操作数据库的mapper
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */

public interface SkinMapper extends BaseMapper<Skin> {
    List<Skin> findAll(QuerySkinVo da0);

    Skin findListItem(GetListItemVo da0);
}